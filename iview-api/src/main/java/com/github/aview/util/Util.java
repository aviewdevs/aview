
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


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Util methods.
 * 
 * @author aview
 */
public class Util {

	/**
	 * Eight-bit Unicode Transformation Format.
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * 
	 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
	 */
	public static final String UTF_8_ENCODING = "UTF-8";

	/**
	 * Eight-bit Unicode Transformation Format.
	 * <p>
	 * Every implementation of the Java platform is required to support this character encoding.
	 * 
	 * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
	 */
	public static final Charset UTF_8 = Charset.forName(UTF_8_ENCODING);

	/**
	 * The MD5 message digest algorithm defined in RFC 1321.
	 */
	public static final String MD5 = "MD5";

	/**
	 * Used to build output as Hex
	 */
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * Used to build output as Hex
	 */
	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	/**
	 * Converts an array of bytes into a String representing the hexadecimal values of each byte in order. The returned
	 * String will be double the length of the passed array, as it takes two characters to represent any given byte.
	 * 
	 * @param data
	 *            a byte[] to convert to Hex characters
	 * @return A String containing hexadecimal characters
	 * @since 1.4
	 */
	public static String encodeHexString(byte[] data) {
		return new String(encodeHex(data));
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
	 * The returned array will be double the length of the passed array, as it takes two characters to represent any
	 * given byte.
	 * 
	 * @param data
	 *            a byte[] to convert to Hex characters
	 * @return A char[] containing hexadecimal characters
	 */
	public static char[] encodeHex(byte[] data) {
		return encodeHex(data, true);
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
	 * The returned array will be double the length of the passed array, as it takes two characters to represent any
	 * given byte.
	 * 
	 * @param data
	 *            a byte[] to convert to Hex characters
	 * @param toLowerCase
	 *            {@code true} converts to lowercase, {@code false} to uppercase
	 * @return A char[] containing hexadecimal characters
	 */
	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
	 * The returned array will be double the length of the passed array, as it takes two characters to represent any
	 * given byte.
	 * 
	 * @param data
	 *            a byte[] to convert to Hex characters
	 * @param toDigits
	 *            the output alphabet
	 * @return A char[] containing hexadecimal characters
	 */
	protected static char[] encodeHex(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data
	 *            Data to digest
	 * @return MD5 digest
	 */
	public static byte[] md5(String data) {
		return md5(data.getBytes(UTF_8));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data
	 *            Data to digest
	 * @return MD5 digest
	 */
	public static byte[] md5(byte[] data) {
		return getMd5Digest().digest(data);
	}

	/**
	 * Returns an MD5 MessageDigest.
	 * 
	 * @return An MD5 digest instance.
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught, which should never happen because MD5 is a
	 *             built-in algorithm
	 * @see MessageDigestAlgorithms#MD5
	 */
	public static MessageDigest getMd5Digest() {
		return getDigest(MD5);
	}

	/**
	 * Returns a <code>MessageDigest</code> for the given <code>algorithm</code> .
	 * 
	 * @param algorithm
	 *            the name of the algorithm requested. See <a href=
	 *            "http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA" >Appendix A in the Java
	 *            Cryptography Architecture API Specification & Reference</a> for information about standard algorithm
	 *            names.
	 * @return An MD5 digest instance.
	 * @see MessageDigest#getInstance(String)
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught.
	 */
	public static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 * 
	 * @param data
	 *            Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(String data) {
		return encodeHexString(md5(data));
	}

	/**
	 * Calculates "unix time", ie seconds after 1970
	 * 
	 * @return The number of seconds since 1970
	 */
	public static long unixtime() {
		return System.currentTimeMillis() / 1000L;
	}

	/**
	 * Returns true if the parameter is null or only whitespace.
	 * 
	 * @param s
	 *            The string to check for only whitespace.
	 * @return true if the paramter is null or only whitespace, false otherwise.
	 */
	public static boolean isBlank(String s) {
		return s == null ? true : s.trim().isEmpty();
	}
}
