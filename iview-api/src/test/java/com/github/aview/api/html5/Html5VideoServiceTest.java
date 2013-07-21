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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;

import org.junit.Ignore;
import org.junit.Test;

import com.github.aview.api.Keyword;
import com.github.aview.api.Series;
import com.github.aview.api.VideoService;
import com.github.aview.api.VideoServiceException;
import com.github.aview.api.html5.Html5Series;
import com.github.aview.api.html5.Html5VideoService;
import com.google.common.util.concurrent.Uninterruptibles;

/**
 * @author aview
 * 
 */
public class Html5VideoServiceTest {

	/**
	 * Test method for {@link com.github.aview.api.html5.Html5VideoService#getAllSeries()}.
	 */
	@Test
	@Ignore
	public void testGetAllSeries() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.github.aview.api.html5.Html5VideoService#getSeriesByKeyword(com.github.aview.api.Keyword)}.
	 */
	@Test
	@Ignore
	public void testGetSeriesByKeyword() throws VideoServiceException {
		Html5VideoService service = new Html5VideoService();
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(3);

		AVRunnable avr1 = new AVRunnable(service, Keyword.FEATURED, startSignal, doneSignal);
		AVRunnable avr2 = new AVRunnable(service, Keyword.LAST_CHANCE, startSignal, doneSignal);
		AVRunnable avr3 = new AVRunnable(service, Keyword.RECENT, startSignal, doneSignal);

		new Thread(avr1).start();
		new Thread(avr2).start();
		new Thread(avr3).start();

		startSignal.countDown();

		Uninterruptibles.awaitUninterruptibly(doneSignal);

		assertNotNull(avr1.response);
		assertNotNull(avr2.response);
		assertNotNull(avr3.response);
	}

	@Test
	public void testSearch() throws VideoServiceException {
		Html5VideoService service = new Html5VideoService();
		Html5Series[] search = service.search("breaking");
		assertNotNull(search);
	}

	private static class AVRunnable implements Runnable {

		VideoService videoService;
		Keyword keyword;
		private final CountDownLatch startSignal;
		private final CountDownLatch doneSignal;

		Series[] response;

		AVRunnable(VideoService videoService, Keyword keyword, CountDownLatch startSignal, CountDownLatch doneSignal) {
			this.videoService = videoService;
			this.keyword = keyword;
			this.startSignal = startSignal;
			this.doneSignal = doneSignal;
		}

		/*
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				startSignal.await();
				response = videoService.getSeriesByKeyword(keyword);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (VideoServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				doneSignal.countDown();
			}
		}

	}

}
