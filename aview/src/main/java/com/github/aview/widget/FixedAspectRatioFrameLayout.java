
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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.github.aview.app.R;

/**
 * @author aview
 * 
 */
public class FixedAspectRatioFrameLayout extends FrameLayout {

	private float ratio;

	public FixedAspectRatioFrameLayout(Context paramContext) {
		super(paramContext);
		this.ratio = 1.0F;
	}

	public FixedAspectRatioFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		TypedArray typedArray = paramContext
				.obtainStyledAttributes(paramAttributeSet, new int[] { R.attr.aspectRatio });
		this.ratio = typedArray.getFraction(0, 1, 1, 1.0F);
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (View.MeasureSpec.getMode(widthMeasureSpec) != View.MeasureSpec.EXACTLY) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(
				(int) (View.MeasureSpec.getSize(widthMeasureSpec) / this.ratio), View.MeasureSpec.EXACTLY));
	}

	public void setAspectRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	public void detachViewFromParent(View child) {
		super.detachViewFromParent(child);
	}

	@Override
	public void attachViewToParent(View child, int index, android.view.ViewGroup.LayoutParams params) {
		super.attachViewToParent(child, index, params);
	}
}
