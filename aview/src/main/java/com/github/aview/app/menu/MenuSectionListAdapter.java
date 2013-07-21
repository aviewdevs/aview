/*
   Copyright 2013 aview authors

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.github.aview.app.menu;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * @author aview
 * 
 */
public class MenuSectionListAdapter extends BaseAdapter {

	private final static String TAG = "MenuSectionListAdapter";

	final ImmutableList<MenuSection> menuSections;
	private Context mContext;
	private LayoutInflater mInflater;
	private int mItemResource;
	private int mFieldId;

	private int mTitleResource;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The current context.
	 * @param itemResource
	 *            The resource ID for a layout file containing a layout to use when instantiating views.
	 * @param textViewResourceId
	 *            The id of the TextView within the layout resource to be populated
	 * @param objects
	 *            The objects to represent in the ListView.
	 */
	public MenuSectionListAdapter(Context context, int titleResource, int itemResource, int textViewResourceId) {
		init(context, titleResource, itemResource, textViewResourceId);
		menuSections = buildMenuSections();
	}

	private ImmutableList<MenuSection> buildMenuSections() {
		Builder<MenuSection> builder = ImmutableList.builder();

		// TODO sub categories
		for (MenuSection title : MenuSection.menu) {
			builder.add(title);
			for (MenuSection item : title) {
				builder.add(item);
			}
		}

		return builder.build();
	}

	private void init(Context context, int titleResource, int resource, int textViewResourceId) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItemResource = resource;
		mTitleResource = titleResource;
		mFieldId = textViewResourceId;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return !menuSections.get(position).isSection();
	}

	@Override
	public int getCount() {
		return menuSections.size();
	}

	@Override
	public MenuSection getItem(int position) {
		return menuSections.get(position);
	}

	/**
	 * Returns the position of the specified item in the array.
	 * 
	 * @param item
	 *            The item to retrieve the position of.
	 * 
	 * @return The position of the specified item.
	 */
	public int getPosition(MenuSection item) {
		return menuSections.indexOf(item);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mItemResource);
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
		View view;
		TextView text;

		MenuSection item = getItem(position);

		if (convertView == null
				|| !convertView.getTag().equals(item.section ? "drawer_title" : "drawer_selectable_item")) {
			view = mInflater.inflate(item.section ? mTitleResource : mItemResource, parent, false);
		} else {
			view = convertView;
		}

		try {
			if (mFieldId == 0) {
				// If no custom field is assigned, assume the whole resource is a TextView
				text = (TextView) view;
			} else {
				// Otherwise, find the TextView field within the layout
				text = (TextView) view.findViewById(mFieldId);
			}
		} catch (ClassCastException e) {
			Log.e(TAG, "You must supply a resource ID for a TextView");
			throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
		}

		text.setText(mContext.getText(item.titleResId));

		return view;
	}

}
