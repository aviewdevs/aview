
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


import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Reader;

import org.junit.Test;


import com.github.aview.api.html5.Rss;
import com.github.aview.api.html5.RssUnmarshaller;
import com.github.aview.util.Util;
import com.google.common.io.Resources;

/**
 * @author aview
 */
public class RssUnmarshallerTest {

    /**
     * Test method for
     * {@link com.github.aview.api.html5.RssUnmarshaller#unmarshallObject(java.lang.String, java.io.Reader, java.lang.Class)}
     * .
     * 
     * @throws IOException
     *             if getting the classpath resources fails
     */
    @Test
    public void testUnmarshallObject() throws IOException {
        Reader reader = Resources.newReaderSupplier(Resources.getResource("rss.xml"), Util.UTF_8).getInput();
        Rss rss = RssUnmarshaller.instance.unmarshallObject("application/xml", reader, Rss.class);
        assertNotNull(rss);
    }
}
