
package com.github.aview.util;

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


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.google.common.io.Closer;
import com.squareup.okhttp.OkHttpClient;

/**
 * @author aview
 * 
 */
public class HttpClient {

	private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private static final String JSON_CONTENT_TYPE = "application/json";
	private static final String ACCEPT_LANGUAGE = "en-gb";
	private static final String DEFAULT_USER_AGENT = "iView/2.1 CFNetwork/548.1.4 Darwin/11.0.0";

	private final GsonUnmarshaller gu;
	private final OkHttpClient httpClient;

	public HttpClient() {
		// TODO Check whether cookie manager and response cache need to be setup
		this(new GsonUnmarshaller(), new OkHttpClient());
	}

	public HttpClient(GsonUnmarshaller gsonUnmarshaller, OkHttpClient httpClient) {
		this.gu = gsonUnmarshaller;
		this.httpClient = httpClient;
	}

	private final Unmarshaller internalUnmarshaller = new Unmarshaller() {
		@Override
		public <E> E unmarshallObject(String contentType, Reader reader, Class<E> type) {
			return HttpClient.this.unmarshallObject(contentType, reader, type);
		}
	};

	private static CookieManager cookieManager;

	static {
		cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);
	}

	public final String getAsString(URL url, Map<String, List<String>> responseHeaders) throws IOException,
			UnauthorizedException {
		return get(url, String.class, internalUnmarshaller, DEFAULT_USER_AGENT, null, null, null, responseHeaders);
	}

	public final <E> E get(URL url, Class<E> type, Unmarshaller unmarshaller, String username, String password)
			throws IOException, UnauthorizedException {
		return get(url, type, unmarshaller, DEFAULT_USER_AGENT, null, username, password, null);
	}

	public final <E> E get(URL url, Class<E> type, Unmarshaller unmarshaller, String username, String password,
			Map<String, List<String>> responseHeaders) throws IOException, UnauthorizedException {
		return get(url, type, unmarshaller, DEFAULT_USER_AGENT, null, username, password, responseHeaders);
	}

	public final <E> E get(URL url, Class<E> type, Unmarshaller unmarshaller, String userAgent, String username,
			String password, Map<String, List<String>> responseHeaders) throws IOException, UnauthorizedException {
		return get(url, type, unmarshaller, userAgent, null, username, password, responseHeaders);
	}

	public final <E> E get(URL url, Class<E> type, Unmarshaller unmarshaller, String userAgent, String referer,
			String username, String password, Map<String, List<String>> responseHeaders) throws IOException,
			UnauthorizedException {
		Reader reader = null;

		Closer closer = Closer.create();

		HttpURLConnection httpUrlConnection = httpClient.open(url);

		try {

			httpUrlConnection.setRequestMethod("GET");

			if (username != null && password != null) {
				httpUrlConnection.setRequestProperty("Authorization",
						"Basic " + Base64.encodeToString((username + ":" + password).getBytes(DEFAULT_CHARSET), false));
			}
			httpUrlConnection.setUseCaches(true);
			httpUrlConnection.setRequestProperty("Host", url.getHost());
			if (!Strings.isNullOrEmpty(userAgent)) {
				httpUrlConnection.setRequestProperty("User-Agent", userAgent);
			}
			if (!Strings.isNullOrEmpty(referer)) {
				httpUrlConnection.setRequestProperty("Referer", referer);
			}
			httpUrlConnection.setRequestProperty("Accept", "*/*");
			httpUrlConnection.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
			httpUrlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			try {
				httpUrlConnection.connect();

				String encoding = httpUrlConnection.getContentEncoding();
				String contentType = httpUrlConnection.getContentType();
				Charset cs = getCharsetFromContentType(contentType);

				if (responseHeaders != null) {
					for (Map.Entry<String, List<String>> entry : httpUrlConnection.getHeaderFields().entrySet()) {
						responseHeaders.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
					}
				}

				InputStream stream = new BufferedInputStream(httpUrlConnection.getInputStream());
				if ("gzip".equals(encoding)) {
					stream = new GZIPInputStream(stream);
				} else if ("deflate".equals(encoding)) {
					stream = new InflaterInputStream(stream);
				}

				reader = closer.register(new InputStreamReader(stream, cs));

				return unmarshaller.unmarshallObject(contentType, reader, type);
			} catch (Throwable e) {
				if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
					throw new UnauthorizedException(e);
				}
				throw closer.rethrow(e);
			}
		} finally {
			closer.close();

			if (httpUrlConnection != null) {
				httpUrlConnection.disconnect();
			}
		}
	}

	public final <E> E get(URL url, Class<E> type, Unmarshaller unmarshaller) throws IOException, UnauthorizedException {
		return get(url, type, unmarshaller, DEFAULT_USER_AGENT, null, null, null, null);
	}

	public final <E> E get(URL url, Class<E> type) throws IOException, UnauthorizedException {
		return get(url, type, internalUnmarshaller, DEFAULT_USER_AGENT, null, null, null, null);
	}

	public final <E> E unmarshallObject(String contentType, Reader reader, Class<E> type) {
		if (String.class.equals(type)) {
			try {
				@SuppressWarnings("unchecked")
				E e = (E) CharStreams.toString(reader);
				return e;
			} catch (IOException e) {
				logger.error("Could not read String from Reader", e);
				throw new RuntimeException(e);
			}
		} else if (!Strings.isNullOrEmpty(contentType) && contentType.trim().startsWith(JSON_CONTENT_TYPE)) {
			E e = gu.unmarshallObject(contentType, reader, type);
			return e;
		} else if (Unmarshallable.class.isAssignableFrom(type)) {
			try {
				E e = type.newInstance();
				((Unmarshallable) e).unmarshall(reader);
				return e;
			} catch (Exception e) {
				logger.error("Could not instantiate object of type {}", type.getName(), e);
				throw new RuntimeException(e);
			}
		} else {
			throw new IllegalArgumentException("Can't create object from content type " + contentType + " and class "
					+ type.getName());
		}
	}

	private static Charset getCharsetFromContentType(String contentType) {
		if (!Strings.isNullOrEmpty(contentType)) {
			int index = contentType.indexOf(';');
			if (index > -1) {
				try {
					return Charset.forName(contentType.substring(index + 1).trim());
				} catch (Exception e) {
					logger.warn("Could not get charset for content type: {}", contentType, e);
				}
			}
		}
		return DEFAULT_CHARSET;
	}

}
