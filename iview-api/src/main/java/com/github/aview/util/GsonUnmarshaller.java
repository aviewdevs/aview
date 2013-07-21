
package com.github.aview.util;

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


import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author aview
 * 
 */
public class GsonUnmarshaller implements Unmarshaller {

	private static final Logger logger = LoggerFactory.getLogger(GsonUnmarshaller.class.getName());

	private static final String JSON_CONTENT_TYPE = "application/json";

	// A Gson instance should be thread safe.
	private final Gson g = new GsonBuilder().registerTypeAdapterFactory(ABCDateTypeAdapter.FACTORY).create();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.aview.util.Unmarshaller#unmarshallObject(java.lang.String,
	 * java.io.Reader)
	 */
	@Override
	public <T> T unmarshallObject(String contentType, Reader reader, Class<T> type) {
		if (Strings.isNullOrEmpty(contentType) || !contentType.trim().startsWith(JSON_CONTENT_TYPE)) {
			logger.warn("This unmarshaller only works on JSON content, content-type: {}", contentType);
		}
		return g.fromJson(reader, type);
	}

}
