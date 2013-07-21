package com.github.aview.app;

/*
 * #%L
 * aview
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


public enum Rating {

	NONE("", null), CTC("ctc", com.github.aview.app.R.drawable.icon_ctc), G("g", com.github.aview.app.R.drawable.icon_g), PG(
			"pg", com.github.aview.app.R.drawable.icon_pg), M("m", com.github.aview.app.R.drawable.icon_m), MA("ma",
			com.github.aview.app.R.drawable.icon_ma), R("r", com.github.aview.app.R.drawable.icon_r), X("x",
			com.github.aview.app.R.drawable.icon_x);

	public final String value;
	public final Integer drawableResourceId;

	Rating(String value, Integer drawableResourceId) {
		this.value = value;
		this.drawableResourceId = drawableResourceId;
	}

	public static Rating fromString(String value) {
		for (Rating r : values()) {
			if (r.value.equalsIgnoreCase(value))
				return r;
		}
		// Default to None
		return NONE;
	}
}
