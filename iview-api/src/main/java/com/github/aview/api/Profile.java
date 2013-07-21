
package com.github.aview.api;

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
 * 
 */
public enum Profile {
    LIVE_HIGH(true), LIVE_BASE(true), LIVE_LOW(true), PROGRESSIVE(false);

    private boolean mobile;

    Profile(boolean mobile) {
        this.mobile = mobile;
    }

    /**
     * Convenience method that wraps valueOf with an Exception handler and returns PROGRESSIVE if the value if invalid.
     * 
     * @param name
     *            The name() of a Profile
     * @return The profile, or progressive if profile name is invalid.
     */
    public static Profile fromString(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return PROGRESSIVE;
        }
    }

	public static Profile fromPrefs(boolean hls, String hlsProfile) {
		if (hls) {
			try {
				return valueOf(hlsProfile);
			} catch (Exception e) {
				return LIVE_HIGH;
			}
		}
		return PROGRESSIVE;
	}

    public boolean isMobile() {
        return mobile;
    }
}
