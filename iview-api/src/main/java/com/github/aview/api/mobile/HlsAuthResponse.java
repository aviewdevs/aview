
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

/**
 * @author aview
 * 
 */
public class HlsAuthResponse {

	private long startTime;
	private long expiryTime;
	private String acl;
	private String hmac;
	private final String original;

	public HlsAuthResponse(String httpResponse) {
		if (httpResponse == null || httpResponse.isEmpty()) {
			throw new IllegalArgumentException("HLS Auth response is empty");
		}

		String[] parts = httpResponse.split("~");

		if (parts.length != 4) {
			throw new IllegalArgumentException("HLS Auth response is formatted in a way I don't expect "
					+ Arrays.toString(parts));
		}

		for (String part : parts) {
			String[] keyValue = part.split("=");

			if (keyValue.length != 2) {
				throw new IllegalArgumentException("HLS Auth response is weird: " + part);
			}

			if ("st".equals(keyValue[0])) {
				startTime = Long.parseLong(keyValue[1]);
			} else if ("exp".equals(keyValue[0])) {
				expiryTime = Long.parseLong(keyValue[1]);
			} else if ("acl".equals(keyValue[0])) {
				acl = keyValue[1];
			} else if ("hmac".equals(keyValue[0])) {
				hmac = keyValue[1];
			}
		}

		original = httpResponse;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @return the expiryTime
	 */
	public long getExpiryTime() {
		return expiryTime;
	}

	/**
	 * @return the acl
	 */
	public String getAcl() {
		return acl;
	}

	/**
	 * @return the hmac
	 */
	public String getHmac() {
		return hmac;
	}

	/**
	 * @return the original
	 */
	public String getOriginal() {
		return original;
	}

}
