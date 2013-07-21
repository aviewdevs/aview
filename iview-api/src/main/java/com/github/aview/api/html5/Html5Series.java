
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


import com.github.aview.api.Series;

/**
 * 
 * 
 * @author aview
 * 
 */
public class Html5Series implements Series {

    private static final long serialVersionUID = 839002936952618047L;

    String                    name;
    String                    thumbnailUrl;
    String                    keywords;
    Item[]                    episodes;

    /*
     * @see com.github.aview.api.Series#getId()
     */
    @Override
    public Integer getId() {
        return 0;
    }

    /*
     * @see com.github.aview.api.Series#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * @see com.github.aview.api.Series#getDescription()
     */
    @Override
    public String getDescription() {
        // No series description in HTML 5 version
        return "";
    }

    /*
     * @see com.github.aview.api.Series#getThumbnailUrl()
     */
    @Override
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /*
     * @see com.github.aview.api.Series#getKeywords()
     */
    @Override
    public String getKeywords() {
        return keywords;
    }

    /*
     * @see com.github.aview.api.Series#getEpisodes()
     */
    @Override
    public Item[] getEpisodes() {
        return episodes;
    }

}
