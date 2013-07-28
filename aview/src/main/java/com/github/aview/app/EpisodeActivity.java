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

import java.net.URI;
import java.text.DateFormat;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.debug.hv.ViewServer;
import com.example.fullscreen.util.SystemUiHider;
import com.example.fullscreen.util.SystemUiHider.OnVisibilityChangeListener;
import com.github.aview.api.Episode;
import com.github.aview.api.Profile;
import com.github.aview.api.VideoServiceException;
import com.github.aview.api.mobile.MobileEpisode;
import com.github.aview.beans.AviewVideoService;
import com.github.aview.util.FormatUtil;
import com.github.aview.widget.FixedAspectRatioFrameLayout;
import com.github.aview.widget.RelativeLayout2;
import com.github.aview.widget.RelativeLayout2.SystemWindowInsetListener;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.Trace;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.fragment_episode)
public class EpisodeActivity extends FragmentActivity implements OnPreparedListener, OnVideoSizeChangedListener,
		OnErrorListener, OnCompletionListener, OnBufferingUpdateListener {

	private static final String TAG = "EpisodesActivity";

	public static final String EPISODE = "Episode";

	@ViewById(R.id.root_layout)
	RelativeLayout2 rootLayout;
	@ViewById(R.id.video_layout)
	FixedAspectRatioFrameLayout videoLayout;
	@ViewById(R.id.scroll_view)
	ScrollView scrollView;

	@ViewById(R.id.episode_video_loading)
	ProgressBar videoLoading;
	@ViewById(R.id.video_item)
	VideoView videoView;

	@ViewById(R.id.series_title)
	TextView seriesTitle;
	@ViewById(R.id.episode_title)
	TextView episodeTitle;
	@ViewById(R.id.episode_aired)
	TextView aired;
	@ViewById(R.id.episode_expires)
	TextView expires;
	@ViewById(R.id.episode_rating)
	TextView rating;
	@ViewById(R.id.episode_warning)
	TextView warning;
	@ViewById(R.id.episode_description)
	TextView description;

	MediaController mediaController;

	ShareActionProvider mShareActionProvider;

	@InstanceState
	Episode episode;

	@InstanceState
	Uri videoUri;
	@InstanceState
	Integer position;
	@InstanceState
	boolean activityStopped;
	@InstanceState
	boolean activityPaused;
	@InstanceState
	HashMap<String, String> additionalHeaders;
	@InstanceState
	boolean initOneTime = false;
	@InstanceState
	boolean mComplete = false;

	@Pref
	AviewPrefs_ aviewPrefs;

	@Bean
	AviewVideoService aviewVideoService;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@AfterInject
	public void afterInject() {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
	}

	@AfterViews
	@Trace(tag = TAG, level = Log.VERBOSE)
	public void init() {
		// Setup the various UI components first so that we can re-init the content view
		// and have AA re-get our views by id.
		Intent intent = getIntent();

		if (episode == null) {
			episode = (Episode) intent.getExtras().get(EPISODE);
		}

		if (seriesTitle != null) {
			if (Strings.isNullOrEmpty(episode.getSeriesTitle()))
				seriesTitle.setVisibility(View.GONE);
			else
				seriesTitle.setText(episode.getSeriesTitle());
		}
		if (episodeTitle != null) {
			if (Strings.isNullOrEmpty(episode.getEpisodeTitle()))
				episodeTitle.setVisibility(View.GONE);
			else
				episodeTitle.setText(episode.getEpisodeTitle());
		}
		if (description != null)
			description.setText(episode.getDescription());

		if (aired != null) {
			String airedText = "A long long time ago";
			if (episode.getAired() != null) {
				airedText = DateFormat.getDateInstance(DateFormat.MEDIUM).format(episode.getAired());
			}
			aired.setText(airedText);
		}

		if (expires != null) {
			String expiresText = "Never expires";
			if (episode.getExpires() != null) {
				int timeLeft = (int) (episode.getExpires().getTime() - System.currentTimeMillis()) / 1000;
				expiresText = "Expires in " + FormatUtil.formatDuration(timeLeft, 2);
			}
			expires.setText(expiresText);
		}

		if (rating != null)
			rating.setText(Strings.nullToEmpty(episode.getRating()));
		if (warning != null)
			warning.setText(Strings.nullToEmpty(episode.getWarning()));

		if (videoView != null) {
			videoView.setOnPreparedListener(this);
			videoView.setOnCompletionListener(this);
			videoView.setOnErrorListener(this);
		}

		setViewPropertiesForOrientation(getResources().getConfiguration());

		rootLayout.setInsetEventListener(new SystemWindowInsetListener() {
			@Override
			public void onSystemWindowInsetsChange(Rect insets) {
				setMediaPlayerPadding(insets);
			}
		});

		// Anything else is only run once
		if (!initOneTime)
			initOneTime();

		setSystemUiHiderState(getResources().getConfiguration());
	}

	private void initOneTime() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

		String title;
		if (Strings.isNullOrEmpty(episode.getSeriesTitle()))
			title = episode.getEpisodeTitle();
		else if (Strings.isNullOrEmpty(episode.getEpisodeTitle()))
			title = episode.getSeriesTitle();
		else
			title = episode.getSeriesTitle() + " " + episode.getEpisodeTitle();
		setTitle(title);

		setupSystemHider(getResources().getConfiguration());

		ViewServer.get(this).addWindow(this);

		authenticateUI();

		initOneTime = true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			delayedHide(100);
	}

	@Trace(tag = TAG, level = Log.VERBOSE)
	void authenticateUI() {
		if (videoLoading != null)
			videoLoading.setVisibility(View.VISIBLE);
		authenticate();
	}

	@Background
	@Trace(tag = TAG, level = Log.VERBOSE)
	void authenticate() {
		Profile profile = getSelectedProfile();
		try {
			if (!aviewVideoService.authenticate(episode, profile)) {
				authenticationFailed();
				return;
			}
			HashMap<String, String> headers = new HashMap<String, String>();
			URI uri = aviewVideoService.getUrlForEpisode(episode, profile, headers);

			if (uri == null) {
				streamUnavailable();
				return;
			}

			playVideo(uri, headers);
		} catch (VideoServiceException e) {
			if (Log.isLoggable(TAG, Log.ERROR))
				Log.e(TAG, "Exception playing video", e);
			asyncError(e);
		}
	}

	/**
	 * Get the currently selected profile out of the shared preferences.
	 * 
	 * @return The shared preferences
	 */
	@Trace(tag = TAG, level = Log.VERBOSE)
	protected Profile getSelectedProfile() {
		try {
			return Profile.fromPrefs(aviewPrefs.pref_hls().get(), aviewPrefs.pref_hlsProfile().get());
		} catch (Exception e) {
			return Profile.PROGRESSIVE;
		}
	}

	@UiThread
	@Trace(tag = TAG, level = Log.VERBOSE)
	void playVideo(URI uri, HashMap<String, String> headers) {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "Playing " + uri);
		Uri uri2 = Uri.parse(uri.toString());

		Log.d(TAG, "Uri2=" + uri2);

		this.videoUri = uri2;
		this.additionalHeaders = headers;

		videoView.setKeepScreenOn(true);
		videoView.setVideoURI(uri2);
	}

	@UiThread
	@Trace(tag = TAG, level = Log.VERBOSE)
	void authenticationFailed() {
		Toast.makeText(this, "Sorry, this video is not available in your country.", Toast.LENGTH_SHORT).show();
		finish();
	}

	@UiThread
	@Trace(tag = TAG, level = Log.VERBOSE)
	void streamUnavailable() {
		Toast.makeText(this, "Sorry, this video is not yet available for your chosen profile.", Toast.LENGTH_SHORT)
				.show();
		finish();
	}

	@UiThread
	@Trace(tag = TAG, level = Log.VERBOSE)
	void asyncError(Exception e) {
		Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate menu resource file.
		getMenuInflater().inflate(R.menu.activity_episodes, menu);

		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);

		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, episode.getEpisodeUrl());
		shareIntent.putExtra(Intent.EXTRA_SUBJECT,
				"Watch " + episode.getSeriesTitle() + " " + episode.getEpisodeTitle() + " on ABC iview!");
		setShareIntent(shareIntent);

		// Return true to display menu
		return true;
	}

	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && (activityStopped || activityPaused)) {
			// authenticateUI(Optional.of(position));
			if (!mComplete)
				restartVideo();
			activityStopped = false;
			activityPaused = false;
		}
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	protected void onPause() {
		videoView.pause();
		this.position = videoView.getCurrentPosition();
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "Saving position " + position);

		activityStopped = false;
		activityPaused = true;
		super.onPause();
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	protected void onResume() {
		super.onResume();

		ViewServer.get(this).setFocusedWindow(this);
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	protected void onStop() {
		activityStopped = true;

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		videoView.stopPlayback();
		ViewServer.get(this).removeWindow(this);
		super.onDestroy();
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public void onPrepared(MediaPlayer mp) {
		if (videoLoading != null)
			videoLoading.setVisibility(View.GONE);

		if (mediaController == null) {
			setMediaController();
		}

		if (episode instanceof MobileEpisode) {
			hlsStart(mp);
		} else {
			regularStart(mp);
		}
	}

	void hlsStart(MediaPlayer mp) {
		startVideoView();

		if (position != null && position > 0) {
			mp.setOnSeekCompleteListener(new OnSeekCompleteListener() {
				@Override
				public void onSeekComplete(MediaPlayer mp) {
					if (Log.isLoggable(TAG, Log.DEBUG))
						Log.d(TAG, "onSeekComplete");
					mp.start();
				}
			});
			mp.seekTo(position);
		}
	}

	void regularStart(MediaPlayer mp) {
		if (position != null) {
			if (Log.isLoggable(TAG, Log.DEBUG))
				Log.d(TAG, "Seeking to " + position);
			mp.seekTo(position);
		}
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "Starting video");
		startVideoView();
	}

	void startVideoView() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();
		if (isScreenOn)
			videoView.start();
	}

	/**
	 * Restarts playing the video. Checks to see if starting the video began playing synchronously or, if async, then
	 * show loading spinner to wait for prepared.
	 * 
	 * TODO Check if need to reauthenticate
	 */
	@Trace(tag = TAG, level = Log.VERBOSE)
	void restartVideo() {
		startVideoView();
		if (!videoView.isPlaying() && videoLoading != null)
			videoLoading.setVisibility(View.VISIBLE);
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public void onCompletion(MediaPlayer mp) {
		mComplete = true;
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Trace(tag = TAG, level = Log.VERBOSE)
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	}

	@SuppressLint("NewApi")
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Inflate the layout for the new orientation but don't attach it because
		// we're going to remove it's video view and put our current one in its place
		// because this will prevent the currently playing vide from re-buffering.
		ViewGroup contentParent = (ViewGroup) findViewById(android.R.id.content);
		LayoutInflater inflater = getLayoutInflater();
		View inflated = inflater.inflate(R.layout.fragment_episode, contentParent, false);

		FixedAspectRatioFrameLayout newFrame = (FixedAspectRatioFrameLayout) inflated.findViewById(R.id.video_layout);

		videoLayout.detachViewFromParent(videoView);
		newFrame.removeView(newFrame.findViewById(R.id.video_item));
		newFrame.attachViewToParent(videoView, 0, videoView.getLayoutParams());

		contentParent.removeViewAt(0);
		setContentView(inflated, inflated.getLayoutParams());

		// reset the media controller so that it has the correct dimensions and location.
		setMediaController();

		// reset the info views.
		videoLayout = newFrame;

		rootLayout.requestLayout();
		// TODO Check this is necessary
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			rootLayout.requestFitSystemWindows();
	}

	void setViewPropertiesForOrientation(Configuration newConfig) {

		Drawable actionBarBackground;

		getActionBar().setDisplayShowTitleEnabled(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

			actionBarBackground = getResources().getDrawable(R.color.black_overlay);

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);

		} else {

			actionBarBackground = getDefaultActionBarBackground().orNull();

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
				getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		getActionBar().setBackgroundDrawable(actionBarBackground);
	}

	void setMediaController() {
		mediaController = new MediaController(this);
		mediaControllerPadding = new Rect(mediaController.getPaddingLeft(), mediaController.getPaddingTop(),
				mediaController.getPaddingRight(), mediaController.getPaddingBottom());
		// mediaController.setFitsSystemWindows(true);
		if (rootLayout != null) {
			Rect insets = rootLayout.getInsets();
			setMediaPlayerPadding(insets);
		}
		videoView.setMediaController(mediaController);
	}

	/**
	 * Set the padding on the media player from the system insets.
	 * 
	 * @param insets
	 *            The insets to apply to the media controller padding
	 */
	protected void setMediaPlayerPadding(Rect insets) {
		if (insets != null && mediaController != null) {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				// landscape, apply left,right,bottom
				mediaController.setPadding(mediaControllerPadding.left + insets.left, mediaControllerPadding.top,
						mediaControllerPadding.right + insets.right, mediaControllerPadding.bottom + insets.bottom);
			} else {
				// portrait, apply left,right
				mediaController.setPadding(mediaControllerPadding.left + insets.left, mediaControllerPadding.top,
						mediaControllerPadding.right + insets.right, mediaControllerPadding.bottom);
			}
		}
	}

	/**
	 * Lookup the Default Action Background for the current theme
	 * 
	 * @return The background (or nothing if it can't be found).
	 */
	Optional<Drawable> getDefaultActionBarBackground() {
		TypedValue typedValue = new TypedValue();

		if (getTheme().resolveAttribute(android.R.attr.actionBarStyle, typedValue, true)) {
			int[] backgroundAttr = new int[] { android.R.attr.background };
			final int indexOfAttrBackground = 0;

			TypedArray a = obtainStyledAttributes(typedValue.data, backgroundAttr);
			Drawable background = a.getDrawable(indexOfAttrBackground);
			a.recycle();

			return Optional.fromNullable(background);
		}

		return Optional.absent();
	}

	/**
	 * @param config
	 */
	private void setupSystemHider(Configuration config) {
		if (mSystemUiHider == null) {
			mSystemUiHider = SystemUiHider.getInstance(this, findViewById(android.R.id.content), true, 3000,
					SystemUiHider.FLAG_HIDE_NAVIGATION);
			mSystemUiHider.setOnVisibilityChangeListener(new OnVisibilityChangeListener() {
				@Override
				public void onVisibilityChange(boolean visible) {
					if (visible && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
						delayedHide(3000);
						if (mediaController != null) {
							mediaController.show();
						}
						// ActionBar actionBar = getActionBar();
						// if (actionBar != null) {
						// actionBar.show();
						// }
					}
				}
			});
		}
	}

	/**
	 * 
	 * @param config
	 */
	void setSystemUiHiderState(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mHideHandler.removeCallbacks(mHideRunnable);
			mSystemUiHider.show();
			mSystemUiHider.disable();
			// getActionBar().show();
		} else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mSystemUiHider.setup();
			delayedHide(100);
		}
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the system UI. This is to prevent the jarring
	 * behavior of controls going away while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			delayedHide(3000);
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			// Have to hide media controller otherwise the system ui won't hide
			// properly.
			if (mediaController != null) {
				mediaController.hide();
			}
			// ActionBar actionBar = getActionBar();
			// if (actionBar != null) {
			// actionBar.hide();
			// }
			if (mSystemUiHider != null) {
				mSystemUiHider.hide();
			}
		}
	};

	private Rect mediaControllerPadding;

	/**
	 * Schedules a call to hide() in [delayMillis] milliseconds, cancelling any previously scheduled calls.
	 */
	public void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
