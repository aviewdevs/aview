
package com.github.aview.api.mobile;

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


import java.util.Arrays;
import java.util.Date;


import com.github.aview.api.Series;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author aview
 * 
 */
public class MobileSeries implements Series {

    private static final long serialVersionUID = -4714586420569623473L;

    @SerializedName("episodes")
    private MobileEpisode[]   episodes;
    @SerializedName("keywords")
    private String            keywords;
    @SerializedName("seriesDescription")
    private String            seriesDescription;
    @SerializedName("seriesExpireDate")
    private Date              seriesExpireDate;
    @SerializedName("seriesId")
    private int               seriesId;
    @SerializedName("seriesNumber")
    private Integer           seriesNumber;
    @SerializedName("seriesPubDate")
    private Date              seriesPubDate;
    @SerializedName("seriesTitle")
    private String            seriesTitle;
    @SerializedName("thumbnail")
    private String            thumbnail;

    /*
     * @see com.github.aview.Series#getPrograms()
     */
    @Override
    public MobileEpisode[] getEpisodes() {
        return episodes;
    }

    /*
     * @see com.github.aview.Series#getKeywords()
     */
    @Override
    public String getKeywords() {
        return keywords;
    }

    /*
     * @see com.github.aview.Series#getDescription()
     */
    @Override
    public String getDescription() {
        return seriesDescription;
    }

    public Date getSeriesExpireDate() {
        return seriesExpireDate;
    }

    /*
     * @see com.github.aview.Series#getId()
     */
    @Override
    public Integer getId() {
        return seriesId;
    }

    public Integer getSeriesNumber() {
        return seriesNumber;
    }

    public Date getSeriesPubDate() {
        return seriesPubDate;
    }

    /*
     * @see com.github.aview.Series#getName()
     */
    @Override
    public String getName() {
        return seriesTitle;
    }

    /*
     * @see com.github.aview.Series#getThumbnailUrl()
     */
    @Override
    public String getThumbnailUrl() {
        return thumbnail;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + seriesId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MobileSeries other = (MobileSeries) obj;
        if (seriesId != other.seriesId)
            return false;
        return true;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MobileSeries [episodes=").append(Arrays.toString(episodes)).append(", keywords=")
                .append(keywords).append(", seriesDescription=").append(seriesDescription)
                .append(", seriesExpireDate=").append(seriesExpireDate).append(", seriesId=").append(seriesId)
                .append(", seriesNumber=").append(seriesNumber).append(", seriesPubDate=").append(seriesPubDate)
                .append(", seriesTitle=").append(seriesTitle).append(", thumbnail=").append(thumbnail).append("]");
        return builder.toString();
    }

}
