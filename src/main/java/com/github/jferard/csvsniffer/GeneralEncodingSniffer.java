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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.*;

/** Very unefficient method: test all available charsets. But the result should be ok */
public class GeneralEncodingSniffer implements Sniffer {
	private Set<Charset> charsets;

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
		if (!stream.markSupported())
			stream = new BufferedInputStream(stream);

		stream.mark(size);
		Map<String, Charset> charsetByName = Charset.availableCharsets();
		this.charsets = new HashSet<Charset>(charsetByName.values());

		Iterator<Charset> it = charsets.iterator();
		byte[] bytes = new byte[size];
		while (it.hasNext()) {
			int count = stream.read(bytes);
			if (count == -1) {
				throw new IOException("Emtpy stream");
			}

			Charset charset = it.next();
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer outputBuffer = CharBuffer.allocate(count);
			CoderResult result = decoder.decode(ByteBuffer.wrap(bytes, 0, count), outputBuffer, false);
			if (result.isError()) {
				it.remove();
			}
			stream.reset();
		}
	}

	public Set<Charset> getCharsets() {
		return this.charsets;
	}
}