
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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.simpleframework.xml.transform.Transform;

/**
 * @author aview
 * 
 */
public class AbcDateTransform implements Transform<Date> {

    private final DateFormat abcFormat = buildABCFormat();

    private static DateFormat buildABCFormat() {
        DateFormat abcFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        abcFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        return abcFormat;
    }

    /*
     * @see org.simpleframework.xml.transform.Transform#read(java.lang.String)
     */
    @Override
    public Date read(String value) throws Exception {
        return abcFormat.parse(value);
    }

    /*
     * @see org.simpleframework.xml.transform.Transform#write(java.lang.Object)
     */
    @Override
    public String write(Date value) throws Exception {
        return abcFormat.format(value);
    }

}
