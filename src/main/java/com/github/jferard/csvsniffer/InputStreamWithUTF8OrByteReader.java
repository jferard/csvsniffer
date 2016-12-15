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
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

public class InputStreamWithUTF8OrByteReader extends Reader {
	private static final int BOM_3 = 0xbf;
	private static final int BOM_2 = 0xbb;
	private static final int BOM_1 = 0xEF;
	private static final int BYTE_MASK = 0xff;

	private InputStream is; // markSupported
	private char[] isoByteMap;

	private InputStreamMixedReader reader;

	InputStreamWithUTF8OrByteReader(InputStream is, char[] isoByteMap)
			throws IOException {
		if (is.markSupported())
			this.is = is;
		else
			this.is = new BufferedInputStream(is);

		this.isoByteMap = isoByteMap;
		this.reader = new InputStreamWithUTF8Reader(is);

		this.is.mark(3);
		if (this.is.read() != BOM_1 || this.is.read() != BOM_2
				|| this.is.read() != BOM_3) {
			this.is.reset();
		}
	}

	@Override
	public void close() throws IOException {
		this.is.close();
	}

	@Override
	public int read(char[] cbuf, int coffset, int clen) throws IOException {
		return this.reader.read(this, cbuf, coffset, clen);
	}

	public void fall() {
		this.reader = new InputStreamWithByteReader(this.is, this.isoByteMap);
	}
}
