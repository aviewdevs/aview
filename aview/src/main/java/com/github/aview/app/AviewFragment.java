package com.github.aview.app;

/*
 * #%L
 * aview
 * %%
 * Copyright (C) 2013 The aview authors
 * %%
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.github.aview.api.Profile;
import com.github.aview.api.VideoServiceException;
import com.github.aview.beans.AviewVideoService;
import com.google.common.collect.Lists;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * @author aview
 * 
 */
@EFragment
public abstract class AviewFragment<T> extends Fragment implements LoaderCallbacks<AsyncResult<T[]>>,
		SharedPreferences.OnSharedPreferenceChangeListener {

	public static final String TAG = "AviewFragment";
	public static final String ARG_KEYWORD = "KEYWORD";
	public static final String ARG_CACHE_KEY = "CACHE_KEY";

	private static final String VIDEO_SERIVCE = "VIDEO_SERIVCE";
	private static final int VIDEO_LOADER_ID = 0;

	@ViewById(R.id.episode_list_progress_bar)
	ProgressBar progressBar;

	@ViewById(R.id.listView)
	GridView listView;

	@Bean
	AviewVideoService videoService;

	@Pref
	AviewPrefs_ aviewPrefs;

	@InstanceState
	Profile lastProfile;

	@InstanceState
	Serializable keyword;

	ArrayList<T> episodes;

	@InstanceState
	long lastRefresh;

	@AfterViews
	void afterViews() {
		if (keyword == null) {
			Bundle args = getArguments();
			keyword = args.getSerializable(ARG_KEYWORD);
		}

		if (episodes == null)
			episodes = Lists.newArrayList();

		listView.setAdapter(getAdapter());

		if (listView.getAdapter().getCount() == 0) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(VIDEO_SERIVCE, videoService);
			bundle.putSerializable(ARG_KEYWORD, keyword);

			getLoaderManager().initLoader(VIDEO_LOADER_ID, bundle, this);
			lastRefresh = System.currentTimeMillis();
		} else if (System.currentTimeMillis() > lastRefresh + TimeUnit.MINUTES.toMillis(30)) {
			reload();
		}
	}

	protected abstract ArrayAdapter<T> getAdapter();

	/**
	 * Reload this fragments list of episodes
	 */
	public void reload() {
		Bundle bundle = new Bundle();
		bundle.putSerializable(VIDEO_SERIVCE, videoService);
		bundle.putSerializable(ARG_KEYWORD, keyword);

		getLoaderManager().restartLoader(VIDEO_LOADER_ID, bundle, this);
		lastRefresh = System.currentTimeMillis();
	}

	@AfterInject
	void afterInject() {
		lastProfile = Profile.fromPrefs(aviewPrefs.pref_hls().get(), aviewPrefs.pref_hlsProfile().get());
	}

	@Override
	public void onResume() {
		super.onResume();
		aviewPrefs.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		aviewPrefs.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	/*
	 * @see
	 * android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.
	 * SharedPreferences, java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if ("pref_hls".equals(key) || "pref_hlsProfile".equals(key)) {
			Profile profile = Profile.fromPrefs(aviewPrefs.pref_hls().get(), aviewPrefs.pref_hlsProfile().get());

			if (profile.isMobile() != lastProfile.isMobile()) {
				reload();
			}

			lastProfile = profile;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// restore episodes
		if (savedInstanceState != null) {
			episodes = (ArrayList<T>) savedInstanceState.getSerializable("episodes");
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save a copy of the list because the loader will be reset after
		// onSaveInstanceState
		outState.putSerializable("episodes", Lists.newArrayList(episodes));
	}

	// *****************************************************************************************
	//
	// Loader call backs
	//
	// *****************************************************************************************

	/*
	 * @see android.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<AsyncResult<T[]>> onCreateLoader(int id, Bundle args) {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "onCreateLoader");

		if (id == VIDEO_LOADER_ID) {
			// show progress bar if list is empty
			if (listView.getAdapter().getCount() == 0) {
				progressBar.setVisibility(View.VISIBLE);
			}

			// notify of start loading
			startLoad();

			if (args != null && args.containsKey(VIDEO_SERIVCE)) {

				final AviewVideoService videoService = (AviewVideoService) args.getSerializable(VIDEO_SERIVCE);
				final Serializable keyword = args.getSerializable(ARG_KEYWORD);

				return new AsyncTaskLoader<AsyncResult<T[]>>(this.getActivity()) {

					private static final int STALE_DELTA = 1800000; // 30m

					private final AviewVideoService mVideoService = videoService;
					private final Serializable mKeyword = keyword;
					private long mLastLoad;
					private AsyncResult<T[]> mResponse;

					@Override
					public AsyncResult<T[]> loadInBackground() {
						// Log.d(TAG, "loadInBackground");
						try {
							return new AsyncResult<T[]>(getData(mVideoService, mKeyword));
						} catch (Exception e) {
							if (Log.isLoggable(TAG, Log.ERROR))
								Log.e(TAG, "Error retrieving data.", e);
							return new AsyncResult<T[]>(e);
						}
					}

					@Override
					public void deliverResult(AsyncResult<T[]> data) {
						// Here we cache our response.
						mResponse = data;
						super.deliverResult(data);
					}

					@Override
					protected void onStartLoading() {
						// If our response is null or we have hung onto it for a long time,
						// then we perform a force load.
						if (mResponse == null || System.currentTimeMillis() - mLastLoad >= STALE_DELTA) {
							forceLoad();
						} else if (mResponse != null) {
							// We have a cached result, so we can just
							// return right away.
							super.deliverResult(mResponse);
						}

						mLastLoad = System.currentTimeMillis();
					}

					@Override
					protected void onStopLoading() {
						// This prevents the AsyncTask backing this
						// loader from completing if it is currently running.
						cancelLoad();
					}

					@Override
					protected void onReset() {
						super.onReset();

						// Stop the Loader if it is currently running.
						onStopLoading();

						// Get rid of our cache if it exists.
						mResponse = null;

						// Reset our stale timer.
						mLastLoad = 0;
					}

				};
			}
		}

		return null;
	}

	protected abstract T[] getData(AviewVideoService videoService, Serializable keyword) throws VideoServiceException;

	/*
	 * @see android.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<AsyncResult<T[]>> arg0, AsyncResult<T[]> arg1) {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "onLoadFinished");
		if (arg1 != null) {
			if (arg1.isError()) {
				Exception e = arg1.exception;
				String message;
				if (e instanceof UnsupportedOperationException)
					message = e.getMessage();
				else if (e instanceof IOException)
					message = "Couldn't contact iview";
				else
					message = "An error has occured.";
				new AlertDialog.Builder(this.getActivity()).setTitle("Error").setMessage(message)
						.setNeutralButton("OK", null).create().show();
			} else {
				@SuppressWarnings("unchecked")
				ArrayAdapter<T> adapter = (ArrayAdapter<T>) listView.getAdapter();
				adapter.clear();
				adapter.addAll(arg1.data);
			}
		}
		progressBar.setVisibility(View.GONE);
		finishLoad();
	}

	/*
	 * @see android.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<AsyncResult<T[]>> arg0) {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "Loader reset " + arg0.toString());
		@SuppressWarnings("unchecked")
		ArrayAdapter<T> adapter = (ArrayAdapter<T>) listView.getAdapter();
		adapter.clear();
	}

	// private LoadCallbacks mLoadCallbacks;
	void startLoad() {
		FragmentActivity activity = getActivity();
		if (activity != null && activity instanceof LoadCallbacks) {
			LoadCallbacks loadCallbacks = (LoadCallbacks) activity;
			loadCallbacks.onLoadStart();
		}
	}

	void finishLoad() {
		FragmentActivity activity = getActivity();
		if (activity != null && activity instanceof LoadCallbacks) {
			LoadCallbacks loadCallbacks = (LoadCallbacks) activity;
			loadCallbacks.onLoadFinished();
		}
	}

	public interface LoadCallbacks {

		void onLoadStart();

		void onLoadFinished();
	}
}
