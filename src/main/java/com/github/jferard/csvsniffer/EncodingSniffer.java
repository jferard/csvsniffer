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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

public class EncodingSniffer implements Sniffer {
	private Charset charset;

	public void sniff(String path, final int size) throws IOException {
		InputStream stream = new FileInputStream(path);
		try {
			this.sniff(stream, size);
		} finally {
			stream.close();
		}
	}

	/**
	 * @param stream
	 *            the input stream
	 * @return
	 * @return UTF-8, US-ASCII or null Charset. If null, the charset can be any
	 *         of the existing "1 byte per char" charsets.
	 * @throws IOException
	 */
	@Override
	public void sniff(InputStream stream, final int size)
			throws IOException {
		this.charset = Constants.US_ASCII;

		UTF8Decoder decoder = new UTF8Decoder(stream);
		if (decoder.gobbleBOM())
			this.charset = Constants.UTF_8;

		try {
			for (int i = 0; i < size; i++) {
				int c = decoder.readUnicodeValue();
				if (c == -1)
					return;
				else if (c >= Constants.B10000000)
					this.charset = Constants.UTF_8;
			}
		} catch (CharacterCodingException e) {
			this.charset = null;
		}
	}

	public Charset getCharset() {
		return this.charset;
	}
}