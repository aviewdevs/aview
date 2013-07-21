
package com.github.aview.util;

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


/**
 * @author aview
 * 
 */
public class FormatUtil {

	public static String formatDuration(int seconds) {
		return formatDuration(seconds, 4);
	}

	public static String formatDuration(int seconds, int sigFigs) {
		int days = seconds / 86400;
		int hours = (seconds % 86400) / 3600;
		int minutes = (seconds % 3600) / 60;
		int secondRemainder = seconds % 60;
		StringBuilder sb = new StringBuilder();
		if (days > 0 && sigFigs > 0) {
			sb.append(days);
			sb.append(days == 1 ? "day " : " days ");
			sigFigs--;
		}
		if (hours > 0 && sigFigs > 0) {
			sb.append(hours);
			sb.append(hours == 1 ? " hour " : " hours ");
			sigFigs--;
		}
		if (minutes > 0 && sigFigs > 0) {
			sb.append(minutes);
			sb.append(minutes == 1 ? " minute " : " minutes ");
			sigFigs--;
		}
		if (secondRemainder > 0 && sigFigs > 0) {
			sb.append(secondRemainder);
			sb.append(secondRemainder == 1 ? " second" : " seconds");
			sigFigs--;
		}

		return sb.toString();
	}

	public static String formatShortDuration(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		int secondRemainder = seconds % 60;
		StringBuilder sb = new StringBuilder();
		if (hours > 0) {
			sb.append(hours);
			sb.append(":");
		}
		if (minutes < 10)
			sb.append('0');
		sb.append(minutes);
		sb.append(":");
		if (secondRemainder < 10)
			sb.append('0');
		sb.append(secondRemainder);

		return sb.toString();
	}

}
