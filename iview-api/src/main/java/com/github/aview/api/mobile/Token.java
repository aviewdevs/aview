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


/**
 * @author aview
 */
import java.io.Serializable;
import java.security.SecureRandom;

import com.github.aview.util.Base64;
import com.github.aview.util.Util;

public class Token implements Serializable {

	private static final long serialVersionUID = 3541029621529685034L;

	private static final int SALT_LENGTH = 0x18;

	private static final String CONSTANT = "dummy-value";
	private static final SecureRandom RANDOM = new SecureRandom();

	private String timestamp;
	private String salt;
	private String hash;

	public static Token getToken() {
		Token token = new Token();

		byte[] bytes = new byte[SALT_LENGTH];
		RANDOM.nextBytes(bytes);

		token.salt = Base64.encodeToString(bytes, false).trim().replace("+", "F");
		token.timestamp = Long.toString(Util.unixtime());

		token.buildHash();

		return token;
	}

	public Token(String salt, String timestamp) {
		this.salt = salt;
		this.timestamp = timestamp;

		buildHash();
	}

	protected Token() {
	}

	protected void buildHash() {
		StringBuilder sb = new StringBuilder(this.timestamp);
		sb.append(this.salt);
		sb.append(CONSTANT);

		this.hash = Util.md5Hex(sb.toString()).toUpperCase();
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
		buildHash();
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
		buildHash();
	}

	public String getHash() {
		return hash;
	}

}
