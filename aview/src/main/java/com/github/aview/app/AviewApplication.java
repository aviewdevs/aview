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


import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.app.Application;
import android.net.http.HttpResponseCache;
import android.util.Log;

import com.github.aview.beans.AviewVideoService;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EApplication;
import com.googlecode.androidannotations.annotations.Trace;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EApplication
public class AviewApplication extends Application {

	private static String TAG = "AviewApplication";

	@Bean
	AviewVideoService aviewVideoService;

	@Pref
	AviewPrefs_ aviewPrefs;

	@AfterInject
	@Trace
	void init() {

		// if (BuildConfig.DEBUG) {
		// aviewPrefs.pref_openedDrawer().put(false);
		// }

		// Create a unique id for this install if one doesn't already exist
		if (!aviewPrefs.ivid().exists()) {
			if (Log.isLoggable(TAG, Log.DEBUG))
				Log.d(TAG, "Creating new ivid");
			aviewPrefs.ivid().put(UUID.randomUUID().toString());
		}

		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "ivid=" + aviewPrefs.ivid().get());

		// Create a cache for automatically caching HTTP requests
		try {
			File httpCacheDir = new File(this.getCacheDir(), "http");
			long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
			HttpResponseCache.install(httpCacheDir, httpCacheSize);
		} catch (IOException e) {
			if (Log.isLoggable(TAG, Log.INFO))
				Log.i(TAG, "HTTP response cache installation failed:" + e);
		}
	}

	@Override
	@Trace
	public void onTerminate() {
		super.onTerminate();
		aviewVideoService.close();

		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}

	}

}
