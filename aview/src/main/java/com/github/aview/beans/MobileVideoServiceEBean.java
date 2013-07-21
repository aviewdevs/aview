
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


import java.io.Closeable;

import android.content.SharedPreferences;
import android.util.Log;

import com.github.aview.api.mobile.IDevice;
import com.github.aview.api.mobile.MobileVideoService;
import com.github.aview.app.AviewPrefs_;
import com.google.common.io.Closeables;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Trace;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

/**
 * @author aview
 * 
 */
@EBean
public class MobileVideoServiceEBean extends MobileVideoService implements
        SharedPreferences.OnSharedPreferenceChangeListener, Closeable {

    private static final long   serialVersionUID = -289400571737207845L;

    private static final String TAG              = "MobileVideoServiceEBean";

    @Pref
    AviewPrefs_                 aviewPrefs;

    private boolean             closed           = false;

    /**
     * Mobile Video Service
     */
    public MobileVideoServiceEBean() {
    }

    @AfterInject
	@Trace(tag = TAG, level = Log.VERBOSE)
    public void init() {
        aviewPrefs.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        setIdevice(IDevice.fromString(aviewPrefs.idevice().get()));
        setIvid(aviewPrefs.ivid().get());
    }

    /*
     * @see
     * android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.
     * SharedPreferences, java.lang.String)
     */
    @Override
	@Trace(tag = TAG, level = Log.VERBOSE)
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("idevice".equals(key)) {
            setIdevice(IDevice.fromString(aviewPrefs.idevice().get()));
        } else if ("ivid".equals(key)) {
            setIvid(aviewPrefs.ivid().get());
        }
    }

    /*
     * @see java.io.Closeable#close()
     */
    @Override
	@Trace(tag = TAG, level = Log.VERBOSE)
    public void close() {
        if (!closed) {
            aviewPrefs.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            closed = true;
        }
    }

    /*
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!closed) {
			if (Log.isLoggable(TAG, Log.ERROR))
				Log.e(TAG, "Closing without call to close");
			Closeables.close(this, false);
        }
    }
}
