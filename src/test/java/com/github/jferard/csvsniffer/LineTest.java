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

import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class LineTest {

	private static final byte COMMA = (byte) ',';
	private static final byte PIPE = (byte)'|';
	private static final Charset CS = Charset.forName("ASCII");

	@Test
	public final void test() {
		Line line = new Line(1);
		for (byte b : "a|b|c".getBytes(CS))
			line.append(b);
		Assert.assertEquals(2, line.getCount(PIPE));
		List<Part> parts = line.asParts(PIPE);
		Assert.assertEquals(3, parts.size());
		Assert.assertEquals('a', parts.get(0).getFirstChar());
		Assert.assertEquals('a', parts.get(0).getLastChar());
		Assert.assertEquals('b', parts.get(1).getFirstChar());
		Assert.assertEquals('b', parts.get(1).getLastChar());
		Assert.assertEquals('c', parts.get(2).getFirstChar());
		Assert.assertEquals('c', parts.get(2).getLastChar());
	}
	
	@Test
	public final void test2() {
		Line line = new Line(1);
		for (byte b : "a|b|c".getBytes(CS))
			line.append(b);
		Assert.assertEquals(0, line.getCount(COMMA));
		List<Part> parts = line.asParts(COMMA);
		Assert.assertEquals(1, parts.size());
		Assert.assertEquals('a', parts.get(0).getFirstChar());
		Assert.assertEquals('c', parts.get(0).getLastChar());
	}
	
}
