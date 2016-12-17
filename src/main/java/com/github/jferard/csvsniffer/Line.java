/*******************************************************************************
 * CSV Sniffer - A simple sniffer to detect file encoding and CSV format of a file
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * 
 * This file is part of CSV Sniffer.
 * 
 * CSV Sniffer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CSV Sniffer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.csvsniffer;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

class Line {
	private byte[] array;
	private int size;

	public Line(int maxSize) {
		this.array = new byte[maxSize];
		this.size = 0;
	}

	public Line(byte[] bytes, int maxSize) {
		this.array = new byte[maxSize];
		System.arraycopy(bytes, 0, this.array, 0, bytes.length);
		this.size = bytes.length;
	}

	public int getSize() {
		return this.size;
	}

	public void append(byte c) {
		assert c >= 0;
		this.array[this.size++] = c;
		this.checkArrayLength();
	}

	protected void checkArrayLength() {
		if (this.size >= this.array.length) {
			byte[] newArray = new byte[this.array.length * 2];
			System.arraycopy(this.array, 0, newArray, 0, this.size);
			this.array = newArray;
		}
	}

	public int getCount(byte c) {
		int count = 0;
		for (int j = 0; j < this.size; j++) {
			if (this.array[j] == c)
				count++;
		}
		return count;
	}

	public List<Part> asParts(byte delim) {
		List<Part> parts = new LinkedList<Part>();
		int from = 0;
		for (int j = 0; j < this.size; j++) {
			if (this.array[j] == delim) {
				parts.add(new Part(this.array, from, j));
				from = j + 1;
			}
		}
		parts.add(new Part(this.array, from, this.size));
		return parts;
	}

	@Override
	public String toString() {
		return new String(this.array, 0, this.size, Charset.forName("ASCII"));
	}

	public byte[] toArray() {
		return this.array;
	}
}
