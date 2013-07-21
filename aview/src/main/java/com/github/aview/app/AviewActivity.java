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


import java.util.HashMap;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.debug.hv.ViewServer;
import com.github.aview.api.Series;
import com.github.aview.app.menu.MenuSection;
import com.github.aview.app.menu.MenuSectionListAdapter;
import com.github.aview.beans.AviewVideoService;
import com.google.common.collect.Maps;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.Trace;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.activity_main)
public class AviewActivity extends FragmentActivity implements AviewFragment.LoadCallbacks {

	private static final String AVIEW_FRAGMENT_TAG = "aview_fragment";

	private static final String TAG = "AviewActivity";

	static final String IMAGE_CACHE_DIR = "thumbs";

	@ViewById(R.id.drawer_layout)
	DrawerLayout drawerLayout;

	@ViewById(R.id.sliding_menu_include)
	ListView slidingMenuInclude;

	@ViewById(R.id.content_frame)
	View gridFragment;

	@Bean
	AviewVideoService videoService;

	@Pref
	AviewPrefs_ aviewPrefs;

	@InstanceState
	HashMap<CharSequence, Fragment.SavedState> fragmentStateCache;

	@InstanceState
	int selectedPosition = -1;

	private View mRefreshIndeterminateProgressView = null;

	ActionBarDrawerToggle mDrawerToggle;

	@InstanceState
	CharSequence mTitle;
	@InstanceState
	CharSequence mDrawerTitle;
	@InstanceState
	Boolean hls;

	@AfterInject
	@Trace(level = Log.DEBUG)
	void afterInject() {
		mTitle = mDrawerTitle = getTitle();
	}

	final Handler handler = new Handler();

	@AfterViews
	@Trace(level = Log.DEBUG)
	void afterViews() {

		if (fragmentStateCache == null)
			fragmentStateCache = Maps.newHashMap();

		if (drawerLayout != null) {
			mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
					R.string.drawer_closed) {

				/** Called when a drawer has settled in a completely closed state. */
				@Override
				public void onDrawerClosed(View drawerView) {
					getActionBar().setTitle(mTitle);
					invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
					super.onDrawerClosed(drawerView);
				}

				/** Called when a drawer has settled in a completely open state. */
				@Override
				public void onDrawerOpened(View drawerView) {
					getActionBar().setTitle(mDrawerTitle);
					invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
					super.onDrawerOpened(drawerView);
				}
			};

			drawerLayout.setDrawerListener(mDrawerToggle);

			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			if (aviewPrefs.pref_openedDrawer().get() == false) {
				if (!handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						drawerLayout.openDrawer(Gravity.LEFT);
					}
				}, 100))
					Log.d(TAG, "Couldn't post runnable");

			}
		}

		slidingMenuInclude.setAdapter(new MenuSectionListAdapter(this, R.layout.drawer_list_title,
				R.layout.drawer_list_item, R.id.drawer_item_text));

		slidingMenuInclude.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectPosition(position);
			}
		});

		if (getSupportFragmentManager().findFragmentByTag(AVIEW_FRAGMENT_TAG) == null) {
			selectPosition(1);
		} else if (selectedPosition > -1) {
			String[] menuItems = getResources().getStringArray(R.array.sliding_menu);
			if (selectedPosition < menuItems.length)
				setTitle(menuItems[selectedPosition]);
		}

		ViewServer.get(this).addWindow(this);
	}

	void selectPosition(int position) {

		MenuSection selectedItem = (MenuSection) slidingMenuInclude.getAdapter().getItem(position);

		if (selectedItem.isSection()) {
			slidingMenuInclude.setItemChecked(selectedPosition, true);
			return;
		}

		if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.LEFT)) {
			drawerLayout.closeDrawer(Gravity.LEFT);
		}

		if (selectedPosition == position)
			return;

		AviewFragment<?> fragment;
		try {
			fragment = (AviewFragment<?>) selectedItem.getFragmentClass().newInstance();
		} catch (InstantiationException e) {
			Log.e(TAG, "shouldn't happen", e);
			return;
		} catch (IllegalAccessException e) {
			Log.e(TAG, "shouldn't happen", e);
			return;
		}

		CharSequence title = getText(selectedItem.getTitleResId());

		Bundle args = new Bundle();
		args.putSerializable(AviewFragment.ARG_KEYWORD, selectedItem.getFragmentArg());
		args.putCharSequence(AviewFragment.ARG_CACHE_KEY, title);

		FragmentManager fragmentManager = getSupportFragmentManager();

		cacheCurrentState(fragmentManager);

		SavedState savedState = fragmentStateCache.get(title);

		fragment.setArguments(args);
		fragment.setInitialSavedState(savedState);

		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, AVIEW_FRAGMENT_TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

		selectedPosition = position;
		slidingMenuInclude.setItemChecked(position, true);
		setTitle(title);
	}

	/**
	 * Cache the state of the current fragment if the current fragment requires it.
	 * 
	 * @param fragmentManager
	 */
	public void cacheCurrentState(FragmentManager fragmentManager) {
		// Find currently selected fragment and cache its state
		Fragment fragment = fragmentManager.findFragmentByTag(AVIEW_FRAGMENT_TAG);

		if (fragment != null && fragment instanceof AviewFragment<?>) {
			AviewFragment<?> currentFragment = (AviewFragment<?>) fragment;

			CharSequence mapKey = (CharSequence) currentFragment.getArguments().get(AviewFragment.ARG_CACHE_KEY);

			if (mapKey != null) {
				if (Log.isLoggable(TAG, Log.DEBUG))
					Log.d(TAG, "Found mapKey");

				fragmentStateCache.put(mapKey, fragmentManager.saveFragmentInstanceState(currentFragment));
			}
		}
	}

	/**
	 * Replace the current fragment with a series list.
	 * 
	 * @param series
	 */
	void selectSeries(Series series) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		cacheCurrentState(fragmentManager);

		if (series.getEpisodes().length == 1) {
			Intent intent = EpisodeActivity_.intent(this).get();
			intent.putExtra(EpisodeActivity.EPISODE, series.getEpisodes()[0]);
			startActivity(intent);
		} else {
			AviewSeriesFragment fragment = AviewSeriesFragment_.builder().series(series).build();

			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, AVIEW_FRAGMENT_TAG)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.setBreadCrumbTitle(getActionBar().getTitle()).addToBackStack("select-series").commit();

			setTitle(series.getName());
		}
	}

	/*
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.LEFT)) {
			drawerLayout.closeDrawer(Gravity.LEFT);
		} else {
			if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
				popActionBarTitle();
			}
			super.onBackPressed();
		}
	}

	private void popActionBarTitle() {
		CharSequence title = getSupportFragmentManager().getBackStackEntryAt(0).getBreadCrumbTitle();
		setTitle(title);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(title);
	}

	@Override
	@Trace(level = Log.DEBUG)
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		MenuItem refresh = menu.findItem(R.id.menu_refresh);
		if (refresh != null && loading) {
			if (mRefreshIndeterminateProgressView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(
						LAYOUT_INFLATER_SERVICE);
				mRefreshIndeterminateProgressView = layoutInflater.inflate(R.layout.actionbar_indeterminate_progress,
						null);
			}
			refresh.setActionView(mRefreshIndeterminateProgressView);
		}

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = drawerLayout != null ? drawerLayout.isDrawerOpen(Gravity.LEFT) : false;
		menu.findItem(R.id.menu_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/*
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	@Trace(level = Log.DEBUG)
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.getItemId() == android.R.id.home) {

			if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
				popActionBarTitle();
				getSupportFragmentManager().popBackStack();
			} else if (drawerLayout != null) {
				if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
					drawerLayout.closeDrawer(Gravity.LEFT);
				} else {
					// user knows how to open the drawer, so we don't need to show
					// them how to open the drawer any more.
					if (aviewPrefs.pref_openedDrawer().get() == false) {
						aviewPrefs.pref_openedDrawer().put(true);
					}
					drawerLayout.openDrawer(Gravity.LEFT);
				}
			}

			return true;
		} else if (item.getItemId() == R.id.menu_refresh) {

			reloadCurrentFragment();

			return true;

		} else if (item.getItemId() == R.id.menu_settings) {

			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Reload the contents of the content fragment
	 */
	void reloadCurrentFragment() {
		Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(AVIEW_FRAGMENT_TAG);
		if (currentFragment instanceof AviewFragment) {
			AviewFragment<?> aviewFragment = (AviewFragment<?>) currentFragment;
			aviewFragment.reload();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ViewServer.get(this).setFocusedWindow(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		boolean hls = aviewPrefs.pref_hls().get();
		if (this.hls == null) {
			this.hls = hls;
		} else if (this.hls != hls) {
			this.hls = hls;
			reloadCurrentFragment();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if (mDrawerToggle != null)
			mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (mDrawerToggle != null)
			mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ViewServer.get(this).removeWindow(this);
	}

	private boolean loading = false;

	@Override
	public void onLoadStart() {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "onLoadStart");
		loading = true;
		invalidateOptionsMenu();
	}

	@Override
	public void onLoadFinished() {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "onLoadFinished");
		loading = false;
		invalidateOptionsMenu();
	}

}
