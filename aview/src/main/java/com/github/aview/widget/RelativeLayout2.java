
package com.github.aview.widget;

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


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Record the insets of the relative layout and apply them when the layout is in portrait mode. Applys 0,0,0,0 padding
 * in Landscape mode to allow for full screen with action bar and UI overlay.
 * 
 * @author aview
 * 
 */
public class RelativeLayout2 extends RelativeLayout {

	private static final String TAG = RelativeLayout2.class.getSimpleName();

	Rect insetsCopy;

	SystemWindowInsetListener mInsetListener;

	/**
	 * @param context
	 */
	public RelativeLayout2(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RelativeLayout2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public RelativeLayout2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setInsetEventListener(SystemWindowInsetListener systemWindowInsetListener) {
		mInsetListener = systemWindowInsetListener;
	}

	public Rect getInsets() {
		return insetsCopy;
	}

	/*
	 * @see android.view.ViewGroup#fitSystemWindows(android.graphics.Rect)
	 */
	@Override
	protected boolean fitSystemWindows(Rect insets) {
		// this.insets = insets;
		// if (Log.isLoggable(TAG, Log.VERBOSE))
		Log.v(TAG, "FIT SYSTEM WINDOWS CALLED (" + insets + ")");

		if (this.insetsCopy == null)
			this.insetsCopy = new Rect(insets);
		else
			this.insetsCopy.set(insets);

		boolean retVal = super.fitSystemWindows(insets);

		if (mInsetListener != null)
			mInsetListener.onSystemWindowInsetsChange(insetsCopy);

		return retVal;
	}

	public static interface SystemWindowInsetListener {

		void onSystemWindowInsetsChange(Rect insets);
	}
}
