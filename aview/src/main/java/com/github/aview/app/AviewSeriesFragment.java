
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


import java.util.Arrays;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.github.aview.api.Episode;
import com.github.aview.api.Series;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.Trace;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author aview
 * 
 */
@EFragment(R.layout.fragment_aview)
public class AviewSeriesFragment extends Fragment {

	@FragmentArg
	Series series;

	@ViewById(R.id.listView)
	GridView listView;

	@ViewById(R.id.episode_list_progress_bar)
	ProgressBar progressBar;

	@AfterViews
	void afterViews() {
		progressBar.setVisibility(View.GONE);

		listView.setAdapter(new SeriesAdapter(getActivity(), R.layout.default_video_item, Arrays.asList(series
				.getEpisodes())));
	}

	@ItemClick(R.id.listView)
	@Trace(level = Log.DEBUG)
	public void onEpisodeSelected(Episode clicked) {
		Intent intent = EpisodeActivity_.intent(getActivity()).get();
		intent.putExtra(EpisodeActivity.EPISODE, clicked);
		startActivity(intent);
	}
}
