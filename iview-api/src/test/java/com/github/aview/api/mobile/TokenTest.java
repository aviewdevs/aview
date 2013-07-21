
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.github.aview.api.mobile.Token;


/**
 * @author aview
 * 
 */
public class TokenTest {

    /**
     * Test method for {@link com.github.aview.api.mobile.Token#getToken()}.
     */
    @Test
    public void testGetToken() {
        Token token = Token.getToken();
        assertNotNull("Returned token was null", token);
        assertNotNull("Returned token salt was null", token.getSalt());
        assertNotNull("Returned token timestamp was null", token.getTimestamp());
        assertNotNull("Returned token hash was null", token.getHash());
    }

    /**
     * Test method for {@link com.github.aview.api.mobile.Token#Token(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testNewToken() {
        String timestamp = "1349873262";
        String salt = "J4jlZuyw5d8WAXFbuV23vJGbiM7ttj2Z";
        String hash = "94223D363C0752B0C9197159DE5255E4";

        Token token = new Token(salt, timestamp);

        assertEquals("Incorrect hash", hash, token.getHash());
    }

}
