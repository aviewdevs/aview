
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


import java.util.Date;


import com.github.aview.api.Episode;
import com.google.gson.annotations.SerializedName;

/**
 * @author aview
 * 
 */
public class MobileEpisode implements Episode {

    private static final long serialVersionUID = 7049187594623093850L;

    @SerializedName("basicTitle")
    private String            basicTitle;
    @SerializedName("description")
    private String            description;
    @SerializedName("duration")
    private int               duration;
    @SerializedName("expireDate")
    private Date              expireDate;
    @SerializedName("id")
    private int               id;
    @SerializedName("pubDate")
    private Date              pubDate;
    @SerializedName("rating")
    private String            rating;
    @SerializedName("seriesId")
    private Integer           seriesId;
    @SerializedName("seriesTitle")
    private String            seriesTitle;
    @SerializedName("shareURL")
    private String            shareURL;
    @SerializedName("streamBase")
    private String            streamBase;
    @SerializedName("streamHigh")
    private String            streamHigh;
    @SerializedName("streamLow")
    private String            streamLow;
    @SerializedName("thumbnail")
    private String            thumbnail;
    @SerializedName("title")
    private String            title;
    @SerializedName("transmitDate")
    private Date              transmitDate;
    @SerializedName("videoAsset")
    private String            videoAsset;
    @SerializedName("warning")
    private String            warning;

    @Override
    public Date getAired() {
        return transmitDate;
    }

    public String getBasicTitle() {
        return basicTitle;
    }

    @Override
    public String getEpisodeTitle() {
        return basicTitle;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getDuration() {
        return duration;
    }

    @Override
    public String getEpisodeUrl() {
        return shareURL;
    }

    @Override
    public Date getExpires() {
        return expireDate;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Date getPublished() {
        return pubDate;
    }

    @Override
    public String getRating() {
        return rating;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public String getStreamBase() {
        return streamBase;
    }

    public String getStreamHigh() {
        return streamHigh;
    }

    public String getStreamLow() {
        return streamLow;
    }

    @Override
    public String getThumbnailUrl() {
        return thumbnail;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getVideoUrl() {
        return videoAsset;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoAsset = videoUrl;
    }

    @Override
    public String getWarning() {
        return warning;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        MobileEpisode other = (MobileEpisode) obj;
        if (id != other.id)
            return false;
        return true;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MobileEpisode [basicTitle=").append(basicTitle).append(", description=").append(description)
                .append(", duration=").append(duration).append(", expireDate=").append(expireDate).append(", id=")
                .append(id).append(", pubDate=").append(pubDate).append(", rating=").append(rating)
                .append(", seriesId=").append(seriesId).append(", seriesTitle=").append(seriesTitle)
                .append(", shareURL=").append(shareURL).append(", streamBase=").append(streamBase)
                .append(", streamHigh=").append(streamHigh).append(", streamLow=").append(streamLow)
                .append(", thumbnail=").append(thumbnail).append(", title=").append(title).append(", transmitDate=")
                .append(transmitDate).append(", videoAsset=").append(videoAsset).append(", warning=").append(warning)
                .append("]");
        return builder.toString();
    }

}
