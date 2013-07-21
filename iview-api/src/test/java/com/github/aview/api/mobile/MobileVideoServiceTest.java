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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.aview.api.Keyword;
import com.github.aview.api.Profile;
import com.github.aview.api.VideoServiceException;

/**
 * @author aview
 * 
 */
public class MobileVideoServiceTest {

	private MobileVideoService service;

	@Before
	public void before() {
		service = new MobileVideoService(IDevice.IPOD41, UUID.randomUUID().toString());
	}

	/**
	 * Test method for {@link com.github.aview.api.mobile.MobileVideoService#getAllSeries()}.
	 */
	@Test
	@Ignore
	public void testGetAllSeries() throws VideoServiceException {
		MobileSeries[] series = service.getAllSeries();
		assertNotNull(series);
	}

	/**
	 * Test method for {@link com.github.aview.api.mobile.MobileVideoService#getSeries(int)}.
	 */
	@Test
	@Ignore
	public void testGetSeriesInt() throws VideoServiceException {
		MobileSeries[] series = service.getAllSeries();
	}

	/**
	 * Test method for {@link com.github.aview.api.mobile.MobileVideoService#getSeries(int[])}.
	 */
	@Test
	@Ignore
	public void testGetSeriesIntArray() {
		// MobileSeries[] series = service.getAllSeries();
	}

	/**
	 * Test method for
	 * {@link com.github.aview.api.mobile.MobileVideoService#getSeriesByKeyword(com.github.aview.api.Keyword)}.
	 * 
	 * @throws VideoServiceException
	 */
	@Test
	@Ignore
	public void testGetSeriesByKeyword() throws VideoServiceException {
		MobileSeries[] featured = service.getSeriesByKeyword(Keyword.FEATURED);
		assertNotNull(featured);
		printSeries(featured);
		MobileSeries[] lastChance = service.getSeriesByKeyword(Keyword.LAST_CHANCE);
		assertNotNull(lastChance);
		printSeries(lastChance);
		MobileSeries[] recent = service.getSeriesByKeyword(Keyword.RECENT);
		assertNotNull(recent);
		printSeries(recent);
	}

	@Test
	@Ignore
	public void testSearch() throws VideoServiceException {
		MobileSeries[] search = service.search("breaking");
		printSeries(search);
	}

	@Test
	@Ignore
	public void testAuth() throws VideoServiceException {
		MobileSeries[] featured = service.getSeriesByKeyword(Keyword.FEATURED);
		assertTrue(service.authenticate(featured[0].getEpisodes()[0], Profile.LIVE_HIGH));

		Map<String, String> addHeaders = new HashMap<String, String>();
		URI uri = service.getUrlForEpisode(featured[0].getEpisodes()[0], null, addHeaders);
		System.out.println(uri);
		assertNotNull(uri);
	}

	private void printSeries(MobileSeries[] seriess) {
		System.out.println();
		for (MobileSeries series : seriess) {
			System.out.println(series);
		}
		System.out.println();
	}
}
