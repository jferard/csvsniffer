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
import java.util.List;

public class CSVSniffer implements Sniffer {

	private CSVConstraints csvConstraints;
	private CSVFormatSniffer csvSniffer;
	private EncodingSniffer encodingSniffer;
	private CSVOptionalHeaderSniffer headerSniffer;

	CSVSniffer(CSVConstraints csvConstraints) {
		this.csvConstraints = csvConstraints;
	}

	@Override
	public void sniff(InputStream inputStream, int size) throws IOException {
		byte[] bytes = new byte[size];

		int c = inputStream.read();
		int i = 0;
		while (c != -1 && i++ < size) {
			bytes[i] = (byte) c;
			c = inputStream.read();
		}

		this.csvSniffer = new CSVFormatSniffer(this.csvConstraints);
		this.encodingSniffer = new EncodingSniffer();
		ParallelSniffer parallelSniffer = new ParallelSniffer(this.csvSniffer,
				this.encodingSniffer);

		InputStream stream = new ByteArrayInputStream(bytes);
		parallelSniffer.sniff(stream, size);

		this.headerSniffer = CSVOptionalHeaderSniffer.getSniffer(
				this.csvSniffer.getDelimiter(), this.csvSniffer.getQuote(),
				this.csvSniffer.getEscape(), this.encodingSniffer.getCharset());

		stream = new ByteArrayInputStream(bytes);
		this.headerSniffer.sniff(stream, size);
	}

	public byte getDelimiter() {
		return this.csvSniffer.getDelimiter();
	}

	public byte getEscape() {
		return this.csvSniffer.getEscape();
	}

	public byte getQuote() {
		return this.csvSniffer.getQuote();
	}

	public Charset getCharset() {
		return this.encodingSniffer.getCharset();
	}
	
	public List<String> getHeader() {
		return this.headerSniffer.getHeader();
	}
}
