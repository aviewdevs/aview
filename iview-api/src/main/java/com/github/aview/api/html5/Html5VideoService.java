package com.github.aview.api.html5;

/*
 * #%L
 * iview-api
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


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.github.aview.api.AlphaKeyword;
import com.github.aview.api.Category;
import com.github.aview.api.Episode;
import com.github.aview.api.Keyword;
import com.github.aview.api.Profile;
import com.github.aview.api.Series;
import com.github.aview.api.VideoService;
import com.github.aview.api.VideoServiceException;
import com.github.aview.util.HttpClient;
import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

/**
 * Html5 Video Service
 * 
 * @author aview
 */
public class Html5VideoService implements VideoService {

	private static final long serialVersionUID = 274970132334206262L;

	private static final String HTML5_BASE_URL = "dummy-value";

	private static final String HTML5_USERNAME = "dummy-value";
	private static final String HTML5_PASSWORD = "dummy-value";

	private String username;
	private String password;

	private final HttpClient httpClient;

	public Html5VideoService() {
		this(new HttpClient(), HTML5_USERNAME, HTML5_PASSWORD);
	}

	public Html5VideoService(String username, String password) {
		this(new HttpClient(), username, password);
	}

	public Html5VideoService(HttpClient httpClient) {
		this(httpClient, HTML5_USERNAME, HTML5_PASSWORD);
	}

	public Html5VideoService(HttpClient httpClient, String username, String password) {
		this.httpClient = httpClient;
		this.username = username;
		this.password = password;
	}

	/*
	 * @see com.github.aview.api.VideoService#getAllSeries()
	 */
	@Override
	public Series[] getAllSeries() throws VideoServiceException {
		return null;
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeries(int)
	 */
	@Override
	public Series getSeries(int seriesId) throws VideoServiceException {
		return null;
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeries(int[])
	 */
	@Override
	public Series[] getSeries(int... seriesId) throws VideoServiceException {
		return null;
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeriesByKeyword(com.github.aview.api.Keyword)
	 */
	@Override
	public Html5Series[] getSeriesByKeyword(Keyword keyword) throws VideoServiceException {
		Rss rss = getRssByKeyword(keyword);

		Html5Series[] retVal = createSeriesArray(rss);

		return retVal;
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeriesByAlpha(com.github.aview.api.AlphaKeyword)
	 */
	@Override
	public Series[] getSeriesByAlpha(AlphaKeyword alphaKeyword) throws VideoServiceException {
		return createSeriesArray(getRssByAlpha(alphaKeyword), true);
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeriesByCategory(com.github.aview.api.Category)
	 */
	@Override
	public Series[] getSeriesByCategory(Category category) throws VideoServiceException {
		String keywordQuery;

		switch (category) {
		case ABC3:
			keywordQuery = "abc3";
			break;
		case ABC4KIDS:
			keywordQuery = "pre-school";
			break;
		case ABC_NEWS_24:
			keywordQuery = "abc4";
			break;
		case ARTS_AND_CULTURE:
			keywordQuery = "arts";
			break;
		case COMEDY:
			keywordQuery = "comedy";
			break;
		case DOCUMENTARY:
			keywordQuery = "docs";
			break;
		case DRAMA:
			keywordQuery = "drama";
			break;
		case EDUCATION:
			keywordQuery = "education";
			break;
		case INDIGENOUS:
			keywordQuery = "indigenous";
			break;
		case LIFESTYLE:
			keywordQuery = "lifestyle";
			break;
		case NEWS_AND_CURRENT_AFFAIRS:
			keywordQuery = "news";
			break;
		case PANEL_AND_DISCUSSION:
			keywordQuery = "panel";
			break;
		case SPORT:
			keywordQuery = "sport";
			break;
		default:
			throw new IllegalArgumentException("category " + category + " unrecognised.");
		}

		Rss rss = getRssByKeyword(keywordQuery);

		return createSeriesArray(rss, true);
	}

	@Override
	public Episode[] getEpisodesByKeyword(Keyword keyword) throws VideoServiceException {
		Rss rss = getRssByKeyword(keyword);

		List<Item> items = rss.getChannel().getItems();

		return items.toArray(new Item[items.size()]);
	}

	/**
	 * Returns an object representing the RSS feed returned by the HTML 5 iview API
	 * 
	 * @param keyword
	 *            The keyword to get the RSS feed for
	 * @return The RSS feed object
	 * @throws VideoServiceException
	 *             If an Exception occurs calling the service
	 */
	public Rss getRssByKeyword(Keyword keyword) throws VideoServiceException {
		String keywordQuery;
		switch (keyword) {
		case FEATURED:
			keywordQuery = "featured";
			break;
		case LAST_CHANCE:
			keywordQuery = "last-chance";
			break;
		case RECENT:
			keywordQuery = "recent";
			break;
		default:
			throw new UnsupportedOperationException("Unsupported keyword: " + keyword);
		}

		return getRssByKeyword(keywordQuery);
	}

	/**
	 * Returns an object representing the RSS feed returned by the HTML 5 iview API
	 * 
	 * @param keyword
	 *            The keyword to get the RSS feed for
	 * @return The RSS feed object
	 * @throws VideoServiceException
	 *             If an Exception occurs calling the service
	 */
	public Rss getRssByAlpha(AlphaKeyword keyword) throws VideoServiceException {
		String keywordQuery;
		switch (keyword) {
		case A_TO_F:
			keywordQuery = "a-f";
			break;
		case G_TO_L:
			keywordQuery = "g-l";
			break;
		case M_TO_R:
			keywordQuery = "m-r";
			break;
		case S_TO_Z:
			keywordQuery = "s-z";
			break;
		case ZERO_TO_NINE:
			keywordQuery = "0-9";
			break;
		default:
			throw new UnsupportedOperationException("Unsupported keyword: " + keyword);
		}

		return getRssByKeyword(keywordQuery);
	}

	/*
	 * @see com.github.aview.api.VideoService#search(java.lang.String)
	 */
	@Override
	public Html5Series[] search(String searchQuery) throws VideoServiceException {
		Rss rss;
		URL url;
		try {
			url = new URL(HTML5_BASE_URL + "?search=" + URLEncoder.encode(searchQuery));
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		}

		try {
			rss = httpClient.get(url, Rss.class, RssUnmarshaller.instance, null, null, username, password, null);
		} catch (Throwable e) {
			Throwables.propagateIfPossible(e);
			throw new VideoServiceException(e);
		}
		return createSeriesArray(rss);
	}

	/**
	 * @param keywordQuery
	 * @return
	 * @throws VideoServiceException
	 */
	private Rss getRssByKeyword(String keywordQuery) throws VideoServiceException {
		Rss rss;
		URL url;
		try {
			url = new URL(HTML5_BASE_URL + "?keyword=" + keywordQuery);
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		}

		try {
			rss = httpClient.get(url, Rss.class, RssUnmarshaller.instance, null, null, username, password, null);
		} catch (Throwable e) {
			Throwables.propagateIfPossible(e);
			throw new VideoServiceException(e);
		}
		return rss;
	}

	/*
	 * @see com.github.aview.api.VideoService#authenticate()
	 */
	@Override
	public boolean authenticate(Episode episode, Profile profile) throws VideoServiceException {
		if (episode instanceof Item) {
			return "allow".equals(((Item) episode).getMediaRestrictionRelationship());
		}
		return false;
	}

	/*
	 * @see com.github.aview.api.VideoService#getUrlForEpisode(com.github.aview.api.Episode,
	 * com.github.aview.api.Profile, java.util.Map)
	 */
	@Override
	public URI getUrlForEpisode(Episode episode, Profile profile, Map<String, String> additionalRequestHeaders)
			throws VideoServiceException {
		if (profile != Profile.PROGRESSIVE)
			throw new IllegalArgumentException("Html5 Video Service only supports progressive Profile");

		URI retVal = URI.create(episode.getVideoUrl());

		return retVal;
	}

	/**
	 * Create a Series Arrary from an RSS feed result
	 * 
	 * @param rss
	 *            The RSS feed to convert
	 * @return The array of Series objects
	 */
	private Html5Series[] createSeriesArray(Rss rss) {
		return createSeriesArray(rss, false);
	}

	/**
	 * Create a Series Arrary from an RSS feed result
	 * 
	 * @param rss
	 *            The RSS feed to convert
	 * @param sort
	 *            Whether to sort the series list after creating it
	 * @return The array of Series objects
	 */
	private Html5Series[] createSeriesArray(Rss rss, boolean sort) {
		Html5Series[] retVal;

		ArrayListMultimap<String, Item> seriesBuckets = ArrayListMultimap.create();
		for (Item item : rss.getChannel().getItems()) {
			seriesBuckets.put(item.getTitle(), item);
		}

		ArrayList<Html5Series> seriesList = Lists.newArrayListWithCapacity(seriesBuckets.keys().size());

		for (String key : seriesBuckets.keySet()) {
			List<Item> items = seriesBuckets.get(key);
			if (items != null && !items.isEmpty()) {
				Html5Series series = new Html5Series();

				series.name = items.get(0).getTitle();
				series.keywords = items.get(0).getCategory();
				series.thumbnailUrl = items.get(0).getMediaThumbnail();

				Item[] episodes = items.toArray(new Item[items.size()]);
				series.episodes = episodes;

				seriesList.add(series);
			}
		}

		if (sort) {
			Collections.sort(seriesList, new Comparator<Html5Series>() {
				@Override
				public int compare(Html5Series o1, Html5Series o2) {
					String lowerTitle1 = o1.name.trim().toLowerCase();
					String lowerTitle2 = o2.name.trim().toLowerCase();

					if (lowerTitle1.startsWith("the ")) {
						lowerTitle1 = lowerTitle1.substring(4);
					}

					if (lowerTitle2.startsWith("the ")) {
						lowerTitle2 = lowerTitle2.substring(4);
					}

					return lowerTitle1.compareTo(lowerTitle2);
				}

			});
		}

		retVal = seriesList.toArray(new Html5Series[seriesList.size()]);
		return retVal;
	}

	/**
	 * @return the username
	 */
	protected String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	protected void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	protected String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	protected void setPassword(String password) {
		this.password = password;
	}

}
