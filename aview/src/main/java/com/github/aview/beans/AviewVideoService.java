package com.github.aview.beans;

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

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

import android.util.Log;

import com.github.aview.api.AlphaKeyword;
import com.github.aview.api.Category;
import com.github.aview.api.Episode;
import com.github.aview.api.Keyword;
import com.github.aview.api.Profile;
import com.github.aview.api.Series;
import com.github.aview.api.VideoService;
import com.github.aview.api.VideoServiceException;
import com.github.aview.api.html5.Html5VideoService;
import com.github.aview.api.mobile.MobileEpisode;
import com.github.aview.app.AviewPrefs_;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Trace;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;

/**
 * Simple decorator to add Android Annotations dependency injection to the iview-api project services.
 * 
 * @author aview
 */
@EBean(scope = Scope.Singleton)
public class AviewVideoService implements VideoService, Serializable {

	private static final long serialVersionUID = -2604463538821547987L;

	private static final String TAG = "AviewVideoService";

	final Html5VideoService html5Bean = new Html5VideoService();

	@Bean
	MobileVideoServiceEBean mobileBean;

	@Pref
	AviewPrefs_ aviewPrefs;

	private final boolean closed = false;

	/*
	 * @see com.github.aview.api.VideoService#getAllSeries()
	 */
	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public Series[] getAllSeries() throws VideoServiceException {
		Profile profile = getCurrentProfile();
		Series[] retVal;
		if (profile.isMobile()) {
			retVal = mobileBean.getAllSeries();
		} else if (profile == Profile.PROGRESSIVE) {
			retVal = html5Bean.getAllSeries();
		} else {
			throw new VideoServiceException("Unknown profile selected " + profile);
		}

		return retVal;
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeries(int)
	 */
	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public Series getSeries(int seriesId) throws VideoServiceException {
		Profile profile = getCurrentProfile();
		Series retVal;
		if (profile.isMobile()) {
			retVal = mobileBean.getSeries(seriesId);
		} else if (profile == Profile.PROGRESSIVE) {
			retVal = html5Bean.getSeries(seriesId);
		} else {
			throw new VideoServiceException("Unknown profile selected " + profile);
		}

		return retVal;
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeries(int[])
	 */
	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public Series[] getSeries(int... seriesId) throws VideoServiceException {
		Profile profile = getCurrentProfile();
		Series[] retVal;
		if (profile.isMobile()) {
			retVal = mobileBean.getSeries(seriesId);
		} else if (profile == Profile.PROGRESSIVE) {
			retVal = html5Bean.getSeries(seriesId);
		} else {
			throw new VideoServiceException("Unknown profile selected " + profile);
		}

		return retVal;
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeriesByKeyword(com.github.aview.api.Keyword)
	 */
	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public Episode[] getEpisodesByKeyword(Keyword keyword) throws VideoServiceException {
		Profile profile = getCurrentProfile();
		Episode[] retVal;
		if (profile.isMobile()) {
			retVal = mobileBean.getEpisodesByKeyword(keyword);
		} else if (profile == Profile.PROGRESSIVE) {
			retVal = html5Bean.getEpisodesByKeyword(keyword);
		} else {
			throw new VideoServiceException("Unknown profile selected " + profile);
		}

		// My Nexus 7 doesn't like me returning retVal, so copy it into a new
		// array
		Episode[] retVal2 = new Episode[retVal.length];
		System.arraycopy(retVal, 0, retVal2, 0, retVal.length);
		return retVal2;
		// return Arrays.asList(retVal).toArray(new Series[0]);
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeriesByKeyword(com.github.aview .api.Keyword)
	 */
	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public Series[] getSeriesByKeyword(Keyword keyword) throws VideoServiceException {
		Profile profile = getCurrentProfile();
		Series[] retVal;
		if (profile.isMobile()) {
			retVal = mobileBean.getSeriesByKeyword(keyword);
		} else if (profile == Profile.PROGRESSIVE) {
			retVal = html5Bean.getSeriesByKeyword(keyword);
		} else {
			throw new VideoServiceException("Unknown profile selected " + profile);
		}

		// My Nexus 7 doesn't like me returning retVal, so copy it into a new array
		Series[] retVal2 = new Series[retVal.length];
		System.arraycopy(retVal, 0, retVal2, 0, retVal.length);
		return retVal2;
		// return Arrays.asList(retVal).toArray(new Series[0]);
	}

	/*
	 * @see com.github.aview.api.VideoService#authenticate(com.github.aview.api.Episode, com.github.aview.api.Profile)
	 */
	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public boolean authenticate(Episode episode, Profile profile) throws VideoServiceException {
		if (episode instanceof MobileEpisode) {
			return mobileBean.authenticate(episode, profile);
		} else {
			return html5Bean.authenticate(episode, profile);
		}
	}

	/*
	 * @see com.github.aview.api.VideoService#getUrlForEpisode(com.github.aview.api.Episode,
	 * com.github.aview.api.Profile, java.util.Map)
	 */
	@Override
	@Trace(tag = TAG, level = Log.VERBOSE)
	public URI getUrlForEpisode(Episode episode, Profile profile, Map<String, String> additionalRequestHeaders)
			throws VideoServiceException {
		if (episode instanceof MobileEpisode) {
			return mobileBean.getUrlForEpisode(episode, profile, additionalRequestHeaders);
		} else {
			return html5Bean.getUrlForEpisode(episode, profile, additionalRequestHeaders);
		}
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeriesByAlpha(com.github.aview.api.AlphaKeyword)
	 */
	@Override
	public Series[] getSeriesByAlpha(AlphaKeyword alphaKeyword) throws VideoServiceException {
		Profile profile = getCurrentProfile();
		Series[] retVal;
		if (profile.isMobile()) {
			retVal = mobileBean.getSeriesByAlpha(alphaKeyword);
		} else if (profile == Profile.PROGRESSIVE) {
			retVal = html5Bean.getSeriesByAlpha(alphaKeyword);
		} else {
			throw new VideoServiceException("Unknown profile selected " + profile);
		}

		// My Nexus 7 doesn't like me returning retVal, so copy it into a new array
		Series[] retVal2 = new Series[retVal.length];
		System.arraycopy(retVal, 0, retVal2, 0, retVal.length);
		return retVal2;
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeriesByCategory(com.github.aview.api.Category)
	 */
	@Override
	public Series[] getSeriesByCategory(Category category) throws VideoServiceException {
		Profile profile = getCurrentProfile();
		Series[] retVal;
		if (profile.isMobile()) {
			retVal = mobileBean.getSeriesByCategory(category);
		} else if (profile == Profile.PROGRESSIVE) {
			retVal = html5Bean.getSeriesByCategory(category);
		} else {
			throw new VideoServiceException("Unknown profile selected " + profile);
		}

		// My Nexus 7 doesn't like me returning retVal, so copy it into a new array
		Series[] retVal2 = new Series[retVal.length];
		System.arraycopy(retVal, 0, retVal2, 0, retVal.length);
		return retVal2;
	}

	/*
	 * @see com.github.aview.api.VideoService#search(java.lang.String)
	 */
	@Override
	public Series[] search(String searchQuery) throws VideoServiceException {
		Profile profile = getCurrentProfile();
		Series[] retVal;
		if (profile.isMobile()) {
			retVal = mobileBean.search(searchQuery);
		} else if (profile == Profile.PROGRESSIVE) {
			retVal = html5Bean.search(searchQuery);
		} else {
			throw new VideoServiceException("Unknown profile selected " + profile);
		}

		// My Nexus 7 doesn't like me returning retVal, so copy it into a new array
		Series[] retVal2 = new Series[retVal.length];
		System.arraycopy(retVal, 0, retVal2, 0, retVal.length);
		return retVal2;
	}

	/**
	 * Get the current profile.
	 * 
	 * @return The profile.
	 */
	public Profile getCurrentProfile() {
		return Profile.fromPrefs(aviewPrefs.pref_hls().get(), aviewPrefs.pref_hlsProfile().get());
	}

	@Trace(tag = TAG, level = Log.VERBOSE)
	public void close() {
		if (!closed) {
			this.mobileBean.close();
		}
	}
}
