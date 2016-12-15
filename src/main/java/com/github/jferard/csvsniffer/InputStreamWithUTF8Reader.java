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

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWithUTF8Reader implements InputStreamMixedReader {
	private static class FallException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	private static final int B00000000 = 0x00;
	private static final int B00111111 = 0x3f;
	private static final int B01111111 = 0x7f;
	private static final int B10000000 = 0x80;
	private static final int B11000000 = 0xc0;
	private static final int B11100000 = 0xe0;
	private static final int B11110000 = 0xf0;
	private static final int B11111000 = 0xf8;
	private static final int B1101100000000000 = 0x36 << 10;
	private static final int B1101110000000000 = 0x37 << 10;

	private static final byte B00011111 = 0x1f;
	private static final byte B00001111 = 0x0f;
	private static final byte B00000111 = 0x07;

	private static final int UNICODE_TRAILING_BYTE_X_BITS = 6;
	
	private InputStream is;
	private int remainingUTF16Char;
	private int curOffset;
	private int charCount;

	InputStreamWithUTF8Reader(InputStream is) {
		this.is = is;
		this.remainingUTF16Char = -1;
	}

	@Override
	public int read(InputStreamWithUTF8OrByteReader parent, char[] cbuf,
			int coffset, int clen) throws IOException {
		if (clen <= 0)
			return 0;

		this.charCount = 0;
		this.curOffset = coffset;

		// EDGE CASE : there was a trailing UTF-16 waiting.
		if (this.remainingUTF16Char != -1) {
			cbuf[this.curOffset++] = (char) this.remainingUTF16Char;
			this.charCount++;
			this.remainingUTF16Char = -1;
		}

		try {
			while (this.charCount < clen) {
				this.is.mark(4);
				int unicodeValue = this.readUnicodeValue();
				if (unicodeValue == -1)
					return this.charCount;

				this.writeUTF16Bytes(cbuf, clen, unicodeValue);
			}
			return this.charCount;
		} catch (FallException e) {
			this.is.reset();
			return this.fallAndFinishRead(parent, cbuf, clen);
		}
	}

	/**
	 * ENCODING IN UTF-16 https://www.ietf.org/rfc/rfc2781.txt, 2.1 Encoding
	 * UTF-16
	 */
	private void writeUTF16Bytes(char[] cbuf, int clen, int unicodeValue) {
		if (unicodeValue < 0x10000) {
			cbuf[this.curOffset++] = (char) unicodeValue;
			this.charCount++;
		} else {
			// U' = yyyyyyyyyyxxxxxxxxxx
			// W1 = 110110yyyyyyyyyy
			// W2 = 110111xxxxxxxxxx
			int y = unicodeValue >> 10;
			int x = unicodeValue - y;
			cbuf[this.curOffset++] = (char) (B1101100000000000 + y);
			this.charCount++;
			if (this.charCount == clen) { // bad luck!
				this.remainingUTF16Char = (char) (B1101110000000000 + x);
			} else {
				cbuf[this.curOffset++] = (char) (B1101110000000000 + x);
				this.charCount++;
			}
		}
	}

	/**
	 * DECODING UTF-8 https://www.ietf.org/rfc/rfc2279.txt
	 * 
	 * @return -1 if end of stream
	 * @return -2 if continue
	 */
	private int readUnicodeValue() throws FallException, IOException {
		int firstByte = this.is.read();
		if (firstByte == -1)
			return -1;

		int unicodeValue = 0;
		int expectedLen;
		// 1) Lead byte analysis
		if ((firstByte & B10000000) == B00000000) { // b1 = 0b0xxxxxxx
			unicodeValue = firstByte;
			expectedLen = 1;
		} else if ((firstByte & B11100000) == B11000000) { // b1 = 0b110xxxxx
			unicodeValue = firstByte & B00011111;
			expectedLen = 2;
		} else if ((firstByte & B11110000) == B11100000) { // b1 = 0b1110xxxx
			unicodeValue = firstByte & B00001111;
			expectedLen = 3;
		} else if ((firstByte & B11111000) == B11110000) { // b1 = 0b11110xxx
			unicodeValue = firstByte & B00000111;
			expectedLen = 4;
		} else { // not a UTF-8 leading byte
			throw new FallException();
		}

		for (int i = 1; i < expectedLen; i++) {
			int trailingByte = this.is.read();
			if (trailingByte == -1) { // abnormal end of trailing bytes
				throw new FallException();
			}

			int ci = trailingByte & B01111111; // 0b10xxxxxx &
												// 0b01111111 ==
												// 0b00xxxxxx
			if (ci <= B00111111) { // UTF-8 trailing byte : 0b10xxxxxx
				unicodeValue = (unicodeValue << UNICODE_TRAILING_BYTE_X_BITS)
						+ ci;
			} else { // abnormal trailing byte
				throw new FallException();
			}
		}
		return unicodeValue;
	}

	/**
	 * Try to use our spare wheel...
	 * 
	 * @param parent
	 * @param cbuf
	 * @param clen
	 * @return
	 * @throws IOException
	 */
	private int fallAndFinishRead(InputStreamWithUTF8OrByteReader parent,
			char[] cbuf, int clen) throws IOException {
		parent.fall();
		int read = parent.read(cbuf, this.curOffset, clen - this.charCount);
		if (read == -1)
			return this.charCount;
		else
			return this.charCount + read;
	}
}