package com.github.aview.api.mobile;

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

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
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
import com.github.aview.util.UnauthorizedException;
import com.github.aview.util.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * HTTP Live Streaming video service for, ahem, mobile devices.
 * 
 * @author aview
 */
public class MobileVideoService implements VideoService, Serializable {

	public static final String IPOD_UA = "AppleCoreMedia/1.0.0.9B206 (iPod; U; CPU OS 5_1_1 like Mac OS X; en_gb)";
	public static final String IPHONE_UA = "AppleCoreMedia/1.0.0.9B206 (iPhone; U; CPU OS 5_1_1 like Mac OS X; en_gb)";
	public static final String IPAD_UA = "AppleCoreMedia/1.0.0.9B206 (iPad; U; CPU OS 5_1_1 like Mac OS X; en_gb)";

	private static final long serialVersionUID = -352969717268545615L;

	private static final String MOBILE_HOST = "dummy-value";

	private static final String MOBILE_URL = MOBILE_HOST + "dummy-value";

	private static final String HLS_AUTH_URL = MOBILE_HOST + "dummy-value";

	private static final String GEOTEST_URL = "dummy-value";

	private IDevice idevice;

	// private final String deviceId;

	private String ivid;

	private final HttpClient httpClient;

	// TODO Better cache impl
	private final HashMap<Integer, HlsAuthResponse> hmacCache = new HashMap<Integer, HlsAuthResponse>();

	/**
	 * No arg constructor for dependency injection
	 */
	public MobileVideoService() {
		this(new HttpClient());
	}

	public MobileVideoService(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public MobileVideoService(IDevice idevice, String ivid) {
		this(new HttpClient());
		this.idevice = idevice;
		this.ivid = ivid;
	}

	/**
	 * @return the idevice
	 */
	public IDevice getIdevice() {
		return idevice;
	}

	public void setIdevice(IDevice idevice) {
		this.idevice = idevice;
	}

	/*
	 * @see com.github.aview.VideoService#getAllSeries()
	 */
	@Override
	public MobileSeries[] getAllSeries() throws VideoServiceException {
		try {
			return httpClient.get(new URL(MOBILE_URL + "keyword=index&d=" + idevice.getDeviceId()),
					MobileSeries[].class);
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		} catch (IOException e) {
			throw new VideoServiceException(e);
		} catch (UnauthorizedException e) {
			throw new VideoServiceException(e);
		}
	}

	/*
	 * @see com.github.aview.VideoService#getSeries(int)
	 */
	@Override
	public MobileSeries getSeries(int seriesId) throws VideoServiceException {
		try {
			return httpClient.get(
					new URL(MOBILE_URL + "series=" + Integer.toString(seriesId) + "&d=" + idevice.getDeviceId()),
					MobileSeries[].class)[0];
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		} catch (IOException e) {
			throw new VideoServiceException(e);
		} catch (UnauthorizedException e) {
			throw new VideoServiceException(e);
		}
	}

	/*
	 * @see com.github.aview.VideoService#getSeries(int[])
	 */
	@Override
	public MobileSeries[] getSeries(int... seriesIds) throws VideoServiceException {

		if (seriesIds == null || seriesIds.length == 0) {
			throw new IllegalArgumentException("seriesId is empty");
		}

		StringBuilder sb = new StringBuilder(Integer.toString(seriesIds[0]));

		for (int i = 1; i < seriesIds.length; ++i) {
			sb.append(",");
			sb.append(Integer.toString(seriesIds[i]));
		}

		try {
			return httpClient.get(new URL(MOBILE_URL + "series=" + sb.toString() + "&d=" + idevice.getDeviceId()),
					MobileSeries[].class);
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		} catch (IOException e) {
			throw new VideoServiceException(e);
		} catch (UnauthorizedException e) {
			throw new VideoServiceException(e);
		}
	}

	/*
	 * @see com.github.aview.VideoService#getSeriesByKeyword(com.github.aview.Keyword )
	 */
	@Override
	public MobileSeries[] getSeriesByKeyword(Keyword keyword) throws VideoServiceException {
		String keywordValue;

		switch (keyword) {
		case FEATURED:
			keywordValue = "featured";
			break;
		case LAST_CHANCE:
			keywordValue = "featured-last-chance";
			break;
		case RECENT:
			keywordValue = "featured-recent";
			break;
		default:
			throw new IllegalArgumentException("keyword " + keyword + " unrecognised.");
		}

		try {
			return httpClient.get(new URL(MOBILE_URL + "keyword=" + keywordValue + "&d=" + idevice.getDeviceId()),
					MobileSeries[].class);
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		} catch (IOException e) {
			throw new VideoServiceException(e);
		} catch (UnauthorizedException e) {
			throw new VideoServiceException(e);
		}
	}

	@Override
	public MobileEpisode[] getEpisodesByKeyword(Keyword keyword) throws VideoServiceException {
		MobileSeries[] serieses = getSeriesByKeyword(keyword);

		List<MobileEpisode> episodes = Lists.newArrayListWithCapacity(serieses.length);

		for (MobileSeries series : serieses) {
			episodes.add(series.getEpisodes()[0]);
		}

		return episodes.toArray(new MobileEpisode[episodes.size()]);
	}

	/*
	 * @see com.github.aview.api.VideoService#getSeriesByAlpha(com.github.aview.api.AlphaKeyword)
	 */
	@Override
	public Series[] getSeriesByAlpha(AlphaKeyword alphaKeyword) throws VideoServiceException {
		String keywordQuery;

		switch (alphaKeyword) {
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
			throw new IllegalArgumentException("alphaKeyword " + alphaKeyword + " unrecognised.");
		}

		try {
			return httpClient.get(new URL(MOBILE_URL + "keyword=" + keywordQuery + "&d=" + idevice.getDeviceId()),
					MobileSeries[].class);
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		} catch (IOException e) {
			throw new VideoServiceException(e);
		} catch (UnauthorizedException e) {
			throw new VideoServiceException(e);
		}
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

		try {
			return httpClient.get(new URL(MOBILE_URL + "keyword=" + keywordQuery + "&d=" + idevice.getDeviceId()),
					MobileSeries[].class);
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		} catch (IOException e) {
			throw new VideoServiceException(e);
		} catch (UnauthorizedException e) {
			throw new VideoServiceException(e);
		}
	}

	@Override
	public MobileSeries[] search(String searchQuery) throws VideoServiceException {
		throw new UnsupportedOperationException("HTTP Live Streaming search not implemented yet.");
		// try {
		// return httpClient.get(new URL(MOBILE_URL + "search=" + URLEncoder.encode(searchQuery)),
		// MobileSeries[].class);
		// } catch (Throwable e) {
		// Throwables.propagateIfPossible(e);
		// throw new VideoServiceException(e);
		// }
	}

	/**
	 * Authenticate with the Geo IP test URL. The episode param is ignored for this version of authenticate.
	 * 
	 * @param episode
	 *            The episode to authenticate for
	 * @see com.github.aview.api.VideoService#authenticate()
	 */
	@Override
	public boolean authenticate(Episode episode, Profile profile) throws VideoServiceException {
		String geotest;
		try {
			geotest = httpClient.getAsString(new URL(GEOTEST_URL), null);
		} catch (MalformedURLException e) {
			throw new VideoServiceException(e);
		} catch (IOException e) {
			throw new VideoServiceException(e);
		} catch (UnauthorizedException e) {
			throw new VideoServiceException(e);
		}

		return geotest.contains("au");
	}

	/*
	 * @see com.github.aview.api.VideoService#getUrlForEpisode(com.github.aview.api .Episode, java.util.Map)
	 */
	@Override
	public URI getUrlForEpisode(Episode episode, Profile profile, Map<String, String> additionalRequestHeaders)
			throws VideoServiceException {
		if (episode instanceof MobileEpisode) {
			MobileEpisode mobileEpisode = (MobileEpisode) episode;

			Profile selectedProfile = profile == null ? idevice.getDefaultProfile() : profile;
			String streamUrl;

			switch (selectedProfile) {
			case LIVE_BASE:
				streamUrl = mobileEpisode.getStreamBase();
				break;
			case LIVE_LOW:
				streamUrl = mobileEpisode.getStreamLow();
				break;
			case LIVE_HIGH:
			default:
				streamUrl = mobileEpisode.getStreamHigh();
				break;
			}

			if (streamUrl == null || streamUrl.isEmpty()) {
				// The stream isn't available yet, return null;
				return null;
			}

			HlsAuthResponse har = getHlsAuthResponseFromCache(mobileEpisode);

			if (har == null) {
				Token token = Token.getToken();

				HashMap<String, List<String>> httpHeaders = Maps.newHashMap();
				try {
					URL hlsAuthUrl = new URL(HLS_AUTH_URL + "ts=" + token.getTimestamp() + "&salt=" + token.getSalt()
							+ "&token=" + token.getHash() + "&ivid=" + ivid + "&episodeId="
							+ mobileEpisode.getId().toString() + "&d=" + idevice.getDeviceId());

					String hmac = httpClient.getAsString(hlsAuthUrl, httpHeaders);

					har = new HlsAuthResponse(hmac);

				} catch (MalformedURLException e) {
					throw new VideoServiceException(e);
				} catch (IOException e) {
					throw new VideoServiceException(e);
				} catch (UnauthorizedException e) {
					throw new VideoServiceException(e);
				}
			}

			URI response = URI.create(streamUrl + "?hdnea=" + har.getOriginal());
			additionalRequestHeaders.put("User-Agent", idevice.getDeviceType().getUserAgent());
			return response;
		}
		return null;
	}

	/**
	 * Get a cached hls auth response if it's not yet expired.
	 * 
	 * @param mobileEpisode
	 *            The mobile episode to get the cached response for
	 * @return The cached response or null if there is no response or the response has expired
	 */
	private HlsAuthResponse getHlsAuthResponseFromCache(MobileEpisode mobileEpisode) {
		HlsAuthResponse har = hmacCache.get(mobileEpisode.getId());
		if (har != null) {
			long now = Util.unixtime();
			if (now < har.getExpiryTime()) {
				return har;
			} else {
				hmacCache.remove(mobileEpisode.getId());
			}
		}
		return null;
	}

	/**
	 * @return the ivid
	 */
	protected String getIvid() {
		return ivid;
	}

	/**
	 * @param ivid
	 *            the ivid to set
	 */
	protected void setIvid(String ivid) {
		this.ivid = ivid;
	}

}
