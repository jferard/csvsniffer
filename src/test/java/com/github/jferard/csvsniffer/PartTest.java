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

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class PartTest {

	@Test
	public final void test1() throws UnsupportedEncodingException {
		byte[] arr = "abcde".getBytes("ASCII");
		Part part = new Part(arr, 0, arr.length);
		Assert.assertEquals('a', part.getFirstChar());
		Assert.assertEquals('e', part.getLastChar());
		
		Assert.assertEquals('c', part.findCharBefore('d'));
		Assert.assertEquals(-1, part.findCharBefore('z'));
	}
	
	@Test
	public final void test2() throws UnsupportedEncodingException {
		byte[] arr = "   abcde ".getBytes("ASCII");
		Part part = new Part(arr, 0, arr.length);
		part.trim();
		Assert.assertEquals('a', part.getFirstChar());
		Assert.assertEquals('e', part.getLastChar());
		
		Assert.assertEquals('c', part.findCharBefore('d'));
		Assert.assertEquals(-1, part.findCharBefore('z'));
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testEmptyArray() throws UnsupportedEncodingException {
		byte[] arr = "".getBytes("ASCII");
		Part part = new Part(arr, 1, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testEmptyPart() throws UnsupportedEncodingException {
		byte[] arr = "   abcde ".getBytes("ASCII");
		Part part = new Part(arr, 0, 0);
	}
}
