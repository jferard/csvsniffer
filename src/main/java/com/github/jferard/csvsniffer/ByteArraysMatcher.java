package com.github.jferard.csvsniffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** A ByteArray matcher is a tool that compares a set of byte arrays to the bytes read from a stream.
 * It leaves the set unmodified, but consumes bytes from the stream.
 * @author Julien F�rard
 *
 */
class ByteArraysMatcher {
	private final Set<byte[]> remainingByteArrays;
	private final InputStream is;
	private int max;
	private byte[] byteArray;
	
	public ByteArraysMatcher(Set<byte[]> byteArrays, InputStream is) {
		if (byteArrays.isEmpty())
			throw new IllegalArgumentException();
		
		this.remainingByteArrays = new HashSet<byte[]>(byteArrays); // we need a copy
		max = getRemainingByteArraysMaxLength();
		if (max == 0)
			throw new IllegalArgumentException();
		
		this.is = is;
		
	}

	public int getRemainingByteArraysMaxLength() {
		int len = 0; 
        for (byte[] byteArray : this.remainingByteArrays) {
            if (byteArray.length > len)
                len = byteArray.length;
        }
        return len;
	}

	/** @return null if no match found 
	 * @throws IOException */
	public byte[] shortestMatch() throws IOException {
		this.byteArray = null;
		for (int i = 0; i < max; i++) {
			int c = is.read();
			if (c == -1)
				break;

			this.filterByteArraysOnNthByte(i, (byte) c);
			this.checkByteArraysMatch(i);
			if (this.noByteArrayOrByteArrayFound())
				break;
		}
		return this.byteArray;
	}
	
	/** @return null if no match found 
	 * @throws IOException */
	public byte[] longestMatch() throws IOException {
		this.byteArray = null;
		for (int i = 0; i < max; i++) {
			int c = is.read();
			if (c == -1)
				break;

			this.filterByteArraysOnNthByte(i, (byte) c);
			this.checkByteArraysMatch(i);
			if (this.noByteArray())
				break;
		}
		return this.byteArray;
	}

	private void filterByteArraysOnNthByte(int i, byte c) {
		Iterator<byte[]> it = remainingByteArrays.iterator();
		while (it.hasNext()) {
			byte[] bytes = it.next();
			if (i > bytes.length-1  || bytes[i] != c)
				it.remove();
		}
	}

	private void checkByteArraysMatch(int i) {
		for (final byte[] byteArray : this.remainingByteArrays) {
			if (i == byteArray.length - 1) { // ok jusqu'au dernier byte du ByteArray
				this.byteArray = byteArray;
			}
		}
	}

	private boolean noByteArrayOrByteArrayFound() {
		return remainingByteArrays.size() == 0 || this.byteArray != null;
	}
	
	private boolean noByteArray() {
		return remainingByteArrays.size() == 0;
	}
}