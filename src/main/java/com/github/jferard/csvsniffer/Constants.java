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

/**
 * Refs : https://docs.oracle.com/javase/6/docs/technotes/guides/intl/encoding.doc.html
 * 
 * 
 * @author Julien Férard (C) 2016
 *
 */
public class Constants {
	public final static Charset UTF_8 = Charset.forName("UTF-8");
	public final static Charset US_ASCII = Charset.forName("US-ASCII");
	static final int B00000000 = 0x00;
	static final byte B00000111 = 0x07;
	static final byte B00001111 = 0x0f;
	static final byte B00011111 = 0x1f;
	static final int B00111111 = 0x3f;
	static final int B01111111 = 0x7f;
	static final int B10000000 = 0x80;
	static final int B11000000 = 0xc0;
	static final int B1101100000000000 = 0x36 << 10;
	static final int B1101110000000000 = 0x37 << 10;
	static final int B11100000 = 0xe0;
	static final int B11110000 = 0xf0;
	static final int B11111000 = 0xf8;
	static final int UNICODE_TRAILING_BYTE_X_BITS = 6;
	static final int BOM_1 = 0xEF;
	static final int BOM_2 = 0xbb;
	static final int BOM_3 = 0xbf;
}
