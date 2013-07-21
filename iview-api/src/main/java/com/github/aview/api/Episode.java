
package com.github.aview.api;

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


import java.io.Serializable;
import java.util.Date;

/**
 * @author aview
 * 
 */
public interface Episode extends Serializable {

    // public Integer getId(); // a, id,

	public String getSeriesTitle();

    public String getTitle(); // b, title, title + subTitle

    public String getEpisodeTitle(); // , basicTitle, subTitle

    public String getDescription(); // d, description, description

    // public String getCategory(); e, , category

    public Date getAired(); // h, transmitDate, transmitDate

    public Date getExpires(); // g, expireDate, expireDate

    public Date getPublished(); // f, pubDate, pubDate

    // public Integer getSize(); i, ,

    public Integer getDuration(); // j, duration, media:content/@duration (seconds)

    // public String getEpisodeUrlText(); // k, ,

    public String getEpisodeUrl(); // l, shareUrl, guid/link/media:player

    public String getRating(); // m , rating, rating

    // public String getFilename(); // n,

    public String getWarning(); // o, warning, warning

    public String getVideoUrl(); // r, videoAsset, videoAsset

    public String getThumbnailUrl(); // s, thumbnail, media:thumbnail

    // public Integer isNews24(); t, ,

    // public String getSeriesNumber(); // u, , series

    // public String getEpisodeNumber(); // v, , episode
}
