
package com.github.aview.api.html5;

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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aview.api.Episode;
import com.google.common.base.Strings;

/**
 * TODO use properties instead of fields?
 * 
 * @author aview
 * 
 */
@Root
public class Item implements Episode {

    private static final long   serialVersionUID = 8073266581245682028L;

    private static final Logger logger           = LoggerFactory.getLogger(Item.class);

    @Path("guid")
    @Text
    private String              guid;
    @Path("guid")
    @Attribute
    private boolean             isPermaLink;
    @Element
    private String              link;
    @Element
    private String              title;
    @Element
    private String              subtitle;
    @Element(required = false)
    private Integer             series;
    @Element(required = false)
    private Integer             episode;
    @Element(required = false)
    private String              category;
    @Element(required = false)
    private String              description;
    @Element
    private Date                pubDate;

    // media
    @Namespace(reference = "http://search.yahoo.com/mrss/")
    @Path("content")
    @Attribute(name = "type")
    private String              mediaContentType;
    @Namespace(reference = "http://search.yahoo.com/mrss/")
    @Path("content")
    @Attribute(name = "bitrate")
    private String              mediaContentBitrate;
    @Namespace(reference = "http://search.yahoo.com/mrss/")
    @Path("content")
    @Attribute(name = "duration", required = false)
    // String because once it was delivered as ""
    private String              mediaContentDuration;
    @Namespace(reference = "http://search.yahoo.com/mrss/")
    @Path("thumbnail")
    @Attribute(name = "url")
    private String              mediaThumbnail;
    @Namespace(reference = "http://search.yahoo.com/mrss/")
    @Path("player")
    @Attribute(name = "url")
    private String              mediaPlayer;
    @Namespace(reference = "http://search.yahoo.com/mrss/")
    @Path("restriction")
    @Attribute(name = "relationship")
    private String              mediaRestrictionRelationship;
    @Namespace(reference = "http://search.yahoo.com/mrss/")
    @Path("restriction")
    @Attribute(name = "type")
    private String              mediaRestrictionType;
    @Namespace(reference = "http://search.yahoo.com/mrss/")
    @Path("restriction")
    @Text
    private String              mediaRestriction;

    // abc
    @Namespace(reference = "http://www.abc.net.au/tv/mrss")
    @Element(name = "linkCopy", required = false)
    private String              abcLinkCopy;
    @Namespace(reference = "http://www.abc.net.au/tv/mrss")
    @Element(name = "linkURL", required = false)
    private String              abcLinkURL;
    @Namespace(reference = "http://www.abc.net.au/tv/mrss")
    @Element(name = "videoAsset")
    private String              abcVideoAsset;
    @Namespace(reference = "http://www.abc.net.au/tv/mrss")
    @Element(name = "transmitDate", required = false)
    private Date                abcTransmitDate;
    @Namespace(reference = "http://www.abc.net.au/tv/mrss")
    @Element(name = "expireDate")
    private Date                abcExpireDate;
    @Namespace(reference = "http://www.abc.net.au/tv/mrss")
    @Element(name = "rating", required = false)
    private String              abcRating;
    @Namespace(reference = "http://www.abc.net.au/tv/mrss")
    @Element(name = "warning", required = false)
    private String              abcWarning;

    public String getGuid() {
        return guid;
    }

    public boolean isPermaLink() {
        return isPermaLink;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getEpisodeTitle() {
        return subtitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Integer getSeries() {
        return series;
    }

    public Integer getEpisode() {
        return episode;
    }

    public String getCategory() {
        return Strings.nullToEmpty(category);
    }

    @Override
    public String getDescription() {
        return Strings.nullToEmpty(description);
    }

    public Date getPubDate() {
        return pubDate;
    }

    public String getMediaContentType() {
        return mediaContentType;
    }

    public String getMediaContentBitrate() {
        return mediaContentBitrate;
    }

    public Integer getMediaContentDuration() {
        try {
            return Integer.valueOf(mediaContentDuration);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getMediaThumbnail() {
        return mediaThumbnail;
    }

    public String getMediaPlayer() {
        return mediaPlayer;
    }

    public String getMediaRestrictionType() {
        return mediaRestrictionType;
    }

    public String getMediaRestrictionRelationship() {
        return mediaRestrictionRelationship;
    }

    public String getMediaRestriction() {
        return mediaRestriction;
    }

    public String getAbcLinkCopy() {
        return abcLinkCopy;
    }

    public String getAbcLinkURL() {
        return abcLinkURL;
    }

    public String getAbcVideoAsset() {
        return abcVideoAsset;
    }

    public Date getAbcTransmitDate() {
        return abcTransmitDate;
    }

    public Date getAbcExpireDate() {
        return abcExpireDate;
    }

    public String getAbcRating() {
        return abcRating;
    }

    public String getAbcWarning() {
        return abcWarning;
    }

    /*
     * @see com.github.aview.api.Episode#getAired()
     */
    @Override
    public Date getAired() {
        return abcTransmitDate;
    }

    /*
     * @see com.github.aview.api.Episode#getDuration()
     */
    @Override
    public Integer getDuration() {
        return getMediaContentDuration();
    }

    /*
     * @see com.github.aview.api.Episode#getEpisodeUrl()
     */
    @Override
    public String getEpisodeUrl() {
        return link;
    }

    /*
     * @see com.github.aview.api.Episode#getExpires()
     */
    @Override
    public Date getExpires() {
        return abcExpireDate;
    }

    /*
     * @see com.github.aview.api.Episode#getPublished()
     */
    @Override
    public Date getPublished() {
        return pubDate;
    }

    /*
     * @see com.github.aview.api.Episode#getRating()
     */
    @Override
    public String getRating() {
        return Strings.nullToEmpty(abcRating);
    }

    /*
     * @see com.github.aview.api.Episode#getThumbnailUrl()
     */
    @Override
    public String getThumbnailUrl() {
        return mediaThumbnail;
    }

    /*
     * @see com.github.aview.api.Episode#getVideoUrl()
     */
    @Override
    public String getVideoUrl() {
        return abcVideoAsset;
    }

    /*
     * @see com.github.aview.api.Episode#getWarning()
     */
    @Override
    public String getWarning() {
        return Strings.nullToEmpty(abcWarning);
    }

	// Calculated from title and subtitle
	@Override
	public String getSeriesTitle() {
		if (title.contains(subtitle)) {
			return title.substring(0, title.lastIndexOf(subtitle));
		} else {
			return title;
		}
	}

    // double checked locking to cache id generation
    private volatile Integer id;

    /**
     * Try to get an episode id from this RSS item's GUID.
     * 
     * @return The int id from the guid or null if no id found.
     */
    public Integer getId() {
        Integer result = id;
        if (result == null) {
            synchronized (this) {
                result = id;
                if (result == null) {
                    int index = this.guid.lastIndexOf('/');

                    if (index != -1 && index != this.guid.length() - 1) {
                        String potentialId = this.guid.substring(index + 1);
                        try {
                            id = result = Integer.parseInt(potentialId);
                        } catch (NumberFormatException e) {
                            logger.warn("Extracted {} from {}", new Object[] { potentialId, this.guid, e });
                        }
                    }
                }
            }
        }
        return result;
    }
}
