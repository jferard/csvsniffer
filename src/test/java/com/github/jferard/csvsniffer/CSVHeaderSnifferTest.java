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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jferard.csvsniffer.CSVOptionalHeaderSniffer;
import com.google.common.base.Joiner;

public class CSVHeaderSnifferTest {
	private static final Charset ASCII = Charset.forName("US-ASCII");
	private Joiner joiner;

	@Before
	public void setUp() {
		this.joiner = Joiner.on('\n');
	}

	@Test
	public final void testWithHeader() throws IOException {
		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ',', (byte) 0, (byte) 0, ASCII);
		InputStream stream = new ByteArrayInputStream(
				this.joiner.join("Year,Make,Model", "1997,Ford,E350",
						"2000,Mercury,Cougar").getBytes(ASCII));

		csvHeaderSniffer.sniff(stream, 10000);
		Assert.assertEquals(Arrays.asList("Year", "Make", "Model"),
				csvHeaderSniffer.getHeader());

	}

	@Test
	public final void testWithoutHeader() throws IOException {
		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ',', (byte) 0, (byte) 0, ASCII);
		InputStream stream = new ByteArrayInputStream(this.joiner
				.join("1997,Ford,E350", "2000,Mercury,Cougar").getBytes(ASCII));

		csvHeaderSniffer.sniff(stream, 10000);
		Assert.assertNull(csvHeaderSniffer.getHeader());
	}

	@Test
	public final void testWithHeader2() throws IOException {
		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ',', (byte) '"', (byte) '"', ASCII);
		final String s = this.joiner.join("Year,Make,Model,Description,Price",
				"1997,Ford,E350,\"ac, abs, moon\",3000.00",
				"1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\",4900.00",
				"1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00",
				"1996,Jeep,Grand Cherokee,\"MUST SELL!\n air, moon roof, loaded\",4799.00");
		InputStream stream = new ByteArrayInputStream(s.getBytes(ASCII));

		csvHeaderSniffer.sniff(stream, 10000);
		Assert.assertEquals(
				Arrays.asList("Year", "Make", "Model", "Description", "Price"),
				csvHeaderSniffer.getHeader());

	}

	@Test
	public final void testWithoutHeader2() throws IOException {

		CSVOptionalHeaderSniffer csvHeaderSniffer = CSVOptionalHeaderSniffer
				.getSniffer((byte) ',', (byte) '"', (byte) '"', ASCII);
		final String s = this.joiner.join(
				"1997,Ford,E350,\"ac, abs, moon\",3000.00",
				"1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\",4900.00",
				"1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00",
				"1996,Jeep,Grand Cherokee,\"MUST SELL!\n air, moon roof, loaded\",4799.00");
		InputStream stream = new ByteArrayInputStream(s.getBytes(ASCII));

		csvHeaderSniffer.sniff(stream, 10000);
		Assert.assertNull(csvHeaderSniffer.getHeader());
	}

}
