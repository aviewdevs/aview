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


import java.io.Serializable;
import java.util.Iterator;

import android.support.v4.app.Fragment;

import com.github.aview.api.AlphaKeyword;
import com.github.aview.api.Category;
import com.github.aview.api.Keyword;
import com.github.aview.app.AviewEpisodesFragment_;
import com.github.aview.app.AviewSeriesListFragment_;
import com.github.aview.app.R;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class MenuSection implements Iterable<MenuSection> {
	final static ImmutableList<MenuSection> menu;

	static {
		// TODO Entry categories
		// TODO Read from config file
		Builder<MenuSection> builder = ImmutableList.builder();
		builder.add(
				createSection(
						R.string.sliding_menu_item_sections,
						createEntry(R.string.sliding_menu_item_featured, AviewEpisodesFragment_.class, Keyword.FEATURED),
						createEntry(R.string.sliding_menu_item_recent, AviewEpisodesFragment_.class, Keyword.RECENT),
						createEntry(R.string.sliding_menu_item_last_chance, AviewEpisodesFragment_.class,
								Keyword.LAST_CHANCE)),
				createSection(
						R.string.sliding_menu_item_by_alpha,
						createEntry(R.string.sliding_menu_item_a_f, AviewSeriesListFragment_.class, AlphaKeyword.A_TO_F),
						createEntry(R.string.sliding_menu_item_g_l, AviewSeriesListFragment_.class, AlphaKeyword.G_TO_L),
						createEntry(R.string.sliding_menu_item_m_r, AviewSeriesListFragment_.class, AlphaKeyword.M_TO_R),
						createEntry(R.string.sliding_menu_item_s_z, AviewSeriesListFragment_.class, AlphaKeyword.S_TO_Z),
						createEntry(R.string.sliding_menu_item_0_9, AviewSeriesListFragment_.class,
								AlphaKeyword.ZERO_TO_NINE)),
				createSection(
						R.string.sliding_menu_item_by_category,
						createEntry(R.string.sliding_menu_item_abc4kids, AviewSeriesListFragment_.class,
								Category.ABC4KIDS),
						createEntry(R.string.sliding_menu_item_arts, AviewSeriesListFragment_.class,
								Category.ARTS_AND_CULTURE)),
				createEntry(R.string.sliding_menu_item_comedy, AviewSeriesListFragment_.class, Category.COMEDY),
				createEntry(R.string.sliding_menu_item_documentary, AviewSeriesListFragment_.class,
						Category.DOCUMENTARY),
				createEntry(R.string.sliding_menu_item_drama, AviewSeriesListFragment_.class, Category.DRAMA),
				createEntry(R.string.sliding_menu_item_education, AviewSeriesListFragment_.class, Category.EDUCATION),
				createEntry(R.string.sliding_menu_item_indigenous, AviewSeriesListFragment_.class, Category.INDIGENOUS),
				createEntry(R.string.sliding_menu_item_lifestyle, AviewSeriesListFragment_.class, Category.LIFESTYLE),
				createEntry(R.string.sliding_menu_item_news, AviewSeriesListFragment_.class,
						Category.NEWS_AND_CURRENT_AFFAIRS),
				createEntry(R.string.sliding_menu_item_panel, AviewSeriesListFragment_.class,
						Category.PANEL_AND_DISCUSSION),
				createEntry(R.string.sliding_menu_item_sport, AviewSeriesListFragment_.class, Category.SPORT),
				createEntry(R.string.sliding_menu_item_abc3, AviewSeriesListFragment_.class, Category.ABC3),
				createEntry(R.string.sliding_menu_item_abc24, AviewSeriesListFragment_.class, Category.ABC_NEWS_24));

		menu = builder.build();
	}

	final boolean section;
	final int titleResId;
	final Optional<Integer> iconResId;
	final ImmutableList<MenuSection> children;
	final Class<? extends Fragment> fragmentClass;
	final Serializable fragmentArg;
	private boolean opened = false;

	MenuSection(boolean section, int titleResId, int iconResId, Class<? extends Fragment> fragmentClass,
			Serializable fragmentArg, MenuSection... children) {
		this.section = section;
		this.titleResId = titleResId;
		if (iconResId > 0)
			this.iconResId = Optional.of(iconResId);
		else
			this.iconResId = Optional.absent();
		this.fragmentClass = fragmentClass;
		this.fragmentArg = fragmentArg;
		this.children = ImmutableList.copyOf(children);
	}

	static MenuSection createSection(int titleResId, int iconResId, MenuSection... children) {
		return new MenuSection(true, titleResId, iconResId, null, null, children);
	}

	static MenuSection createSection(int titleResId, MenuSection... children) {
		return new MenuSection(true, titleResId, -1, null, null, children);
	}

	static MenuSection createEntry(int titleResId, int iconResId, Class<? extends Fragment> fragmentClass,
			Serializable fragmentArg, MenuSection... children) {
		return new MenuSection(false, titleResId, iconResId, fragmentClass, fragmentArg, children);
	}

	static MenuSection createEntry(int titleResId, Class<? extends Fragment> fragmentClass, Serializable fragmentArg,
			MenuSection... children) {
		return new MenuSection(false, titleResId, -1, fragmentClass, fragmentArg, children);
	}

	public boolean isSection() {
		return section;
	}

	public int getTitleResId() {
		return titleResId;
	}

	public Optional<Integer> getIconResId() {
		return iconResId;
	}

	public ImmutableList<MenuSection> getChildren() {
		return children;
	}

	public Class<? extends Fragment> getFragmentClass() {
		return fragmentClass;
	}

	public Serializable getFragmentArg() {
		return fragmentArg;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public void open() {
		this.opened = true;
	}

	public void close() {
		this.opened = false;
	}

	public void toggle() {
		this.opened = !this.opened;
	}

	public ImmutableList<MenuSection> flatten() {
		Builder<MenuSection> builder = ImmutableList.builder();
		builder.add(this);

		if (section || opened)
			for (MenuSection child : children)
				builder.addAll(child.flatten());
		return builder.build();
	}

	@Override
	public Iterator<MenuSection> iterator() {
		return children.iterator();
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (section ? 1231 : 1237);
		result = prime * result + titleResId;
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuSection other = (MenuSection) obj;
		if (section != other.section)
			return false;
		if (titleResId != other.titleResId)
			return false;
		return true;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MenuSection [titleResId=").append(titleResId).append("]");
		return builder.toString();
	}

}
