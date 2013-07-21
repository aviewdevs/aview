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

import java.io.Serializable;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.github.aview.api.AlphaKeyword;
import com.github.aview.api.Category;
import com.github.aview.api.Series;
import com.github.aview.api.VideoServiceException;
import com.github.aview.beans.AviewVideoService;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.Trace;

/**
 * @author aview
 * 
 */
@EFragment(R.layout.fragment_aview)
public class AviewSeriesListFragment extends AviewFragment<Series> {

	/*
	 * @see com.github.aview.app.AviewFragment#getAdapter()
	 */
	@Override
	protected ArrayAdapter<Series> getAdapter() {
		return new SeriesListAdapter(this.getActivity(), R.layout.default_video_item, episodes);
	}

	/*
	 * @see com.github.aview.app.AviewFragment#getData(com.github.aview.beans.AviewVideoService, java.io.Serializable)
	 */
	@Override
	protected Series[] getData(AviewVideoService videoService, Serializable keyword) throws VideoServiceException {
		Series[] retVal;
		if (keyword instanceof Category) {
			Category category = (Category) keyword;
			retVal = videoService.getSeriesByCategory(category);
		} else if (keyword instanceof AlphaKeyword) {
			AlphaKeyword alphaKeyword = (AlphaKeyword) keyword;
			retVal = videoService.getSeriesByAlpha(alphaKeyword);
		} else {
			throw new IllegalArgumentException("Unrecognised type of keyword");
		}
		return retVal;
	}

	@ItemClick(R.id.listView)
	@Trace(level = Log.DEBUG)
	public void onSeriesSelected(Series clicked) {
		Activity activity = getActivity();
		if (activity instanceof AviewActivity) {
			AviewActivity aviewActivity = (AviewActivity) activity;
			aviewActivity.selectSeries(clicked);
		}
	}
}
