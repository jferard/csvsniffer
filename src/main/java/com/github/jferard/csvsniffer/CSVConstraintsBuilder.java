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

/**
 * The main constraints are : - delimiter, quote and escape can't be a letter or
 * a digit. - quote will be added to escape chars.
 */
public class CSVConstraintsBuilder {
	private static final int ASCII_CHAR_COUNT = 128;
	private byte[] allowedDelimiters;
	private byte[] allowedQuotes;
	private int minFields;
	private byte[] allowedEscapes;

	private static byte all[];

	CSVConstraintsBuilder() {
		this.allowedDelimiters = getAllChars();
		this.allowedQuotes = getAllChars();
		this.allowedEscapes = getAllChars();
		this.minFields = 2;
	}

	private static byte[] getAllChars() {
		if (CSVConstraintsBuilder.all == null) {
			all = new byte[ASCII_CHAR_COUNT-10-26*2-3];
			int i = 0;
			// hack to avoid endless loop : b ALWAYS < 128
			for (byte b = 0; b >= 0 ; b++) {  
				if (b != ' ' && b != '\r' && b != '\n'
						&& (b < '0' || b > '9' && b < 'A' || b > 'Z' && b < 'a'
								|| b > 'z')) {
					all[i++] = b;
				}
			}
		}
		return CSVConstraintsBuilder.all;
	}

	public CSVConstraintsBuilder allowedDelimiters(byte[] allowedDelimiters) {
		this.allowedDelimiters = allowedDelimiters;
		return this;
	}

	public CSVConstraintsBuilder allowedQuotes(byte[] allowedQuotes) {
		this.allowedQuotes = allowedQuotes;
		return this;
	}

	public CSVConstraintsBuilder allowedEscapes(byte[] allowedEscapes) {
		this.allowedEscapes = allowedEscapes;
		return this;
	}

	public CSVConstraintsBuilder minFields(int minFields) {
		this.minFields = minFields;
		return this;
	}

	public CSVConstraints build() {
		return new CSVConstraints(this.allowedDelimiters, this.allowedQuotes,
				this.allowedEscapes, this.minFields);
	}

}
