
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


import java.util.List;
import java.util.NoSuchElementException;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aview.api.Episode;
import com.github.aview.api.Series;
import com.github.aview.util.FormatUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.squareup.picasso.Picasso;

/**
 * @author aview
 * 
 */
public class SeriesListAdapter extends ArrayAdapter<Series> {

	private static final String TAG = "SeriesAdapter";

	private final int itemViewResourceId;

	private final LayoutInflater mInflater;

	/**
	 * @param context
	 * @param itemViewResourceId
	 * @param imageFetcher
	 */
	public SeriesListAdapter(Context context, int itemViewResourceId, List<Series> episodes) {
		super(context, itemViewResourceId, episodes);
		if (Log.isLoggable(TAG, Log.VERBOSE))
			Log.v(TAG, "ctor");
		this.itemViewResourceId = itemViewResourceId;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.v(TAG, "getView");
		View view;
		TextView heading, durationView;
		ImageView backgroundImage, ratingImage;

		if (convertView == null) {
			view = mInflater.inflate(itemViewResourceId, parent, false);
		} else {
			view = convertView;
		}

		Series item = getItem(position);

		heading = (TextView) view.findViewById(R.id.title);

		durationView = (TextView) view.findViewById(R.id.duration);
		backgroundImage = (ImageView) view.findViewById(R.id.thumbnail);
		ratingImage = (ImageView) view.findViewById(R.id.rating_image_view);

		String thumbnailUrl = item.getThumbnailUrl();

		if (item.getEpisodes().length == 1) {
			heading.setText(getHeadingText(item));
			String duration;
			if (item.getEpisodes()[0].getDuration() != null) {
				duration = FormatUtil.formatShortDuration(item.getEpisodes()[0].getDuration());
			} else {
				duration = "1 Episode";
			}
			durationView.setText(duration);
		} else {
			heading.setText(item.getName());
			durationView.setText(item.getEpisodes().length + " episodes");
		}

		Picasso picasso = Picasso.with(getContext());
		picasso.setDebugging(BuildConfig.DEBUG);

		if (!Strings.isNullOrEmpty(thumbnailUrl))
			picasso.load(item.getThumbnailUrl()).placeholder(R.drawable.empty_photo).error(R.drawable.ic_unavailable)
					.resizeDimen(R.dimen.image_thumbnail_width, R.dimen.image_thumbnail_height).centerCrop()
					.into(backgroundImage);
		else
			backgroundImage.setImageResource(R.drawable.ic_unavailable);

		Rating rating = getMaxRatingForSeries(item);
		if (rating == Rating.NONE)
			ratingImage.setVisibility(View.GONE);
		else {
			ratingImage.setVisibility(View.VISIBLE);
			picasso.load(rating.drawableResourceId).resizeDimen(R.dimen.oflc_rating_height, R.dimen.oflc_rating_height)
					.into(ratingImage);
			ratingImage.setContentDescription(rating.value);
		}

		return view;
	}

	/**
	 * @param item
	 * @return
	 */
	protected String getHeadingText(Series series) {
		String seriesTitle = series.getName();
		String episodeTitle = series.getEpisodes()[0].getEpisodeTitle();
		if (Strings.isNullOrEmpty(seriesTitle)) {
			return episodeTitle;
		} else if (seriesTitle.equals(episodeTitle)) {
			return seriesTitle;
		} else {
			return seriesTitle + " " + episodeTitle;
		}
	}

	protected Rating getMaxRatingForSeries(Series series) {
		List<Rating> ratings = Lists.newArrayListWithCapacity(series.getEpisodes().length);
		for (Episode episode : series.getEpisodes()) {
			ratings.add(Rating.fromString(episode.getRating()));
		}
		try {
			return Ordering.natural().max(ratings);
		} catch (NoSuchElementException e) {
			return Rating.NONE;
		}
	}
}
