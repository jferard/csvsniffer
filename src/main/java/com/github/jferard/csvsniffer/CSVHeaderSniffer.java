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
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

public class CSVHeaderSniffer implements Sniffer {
	private Charset charset;
	private CSVFormat csvFormat;
	private List<String> header;

	public CSVHeaderSniffer(final byte delimiter, final byte quote,
			final byte escape, final Charset charset) {
		this.charset = charset;
		this.csvFormat = CSVFormat.newFormat((char) delimiter)
				.withQuote((char) quote).withQuoteMode(QuoteMode.MINIMAL)
				.withAllowMissingColumnNames();
		if (escape != quote)
			this.csvFormat = this.csvFormat.withEscape((char) escape);
	}

	@Override
	public void sniff(InputStream inputStream, int size) throws IOException {
		Reader streamReader = new InputStreamReader(inputStream, this.charset);

		CSVParser parser = new CSVParser(streamReader, this.csvFormat);
		Iterator<CSVRecord> iterator = parser.iterator();

		if (iterator.hasNext()) {
			CSVRecord firstRowRecord = iterator.next();
			final int firstRowSize = firstRowRecord.size();

			char[] firstRowSignature = CSVHeaderSniffer.getSignature(firstRowRecord,
					firstRowSize);
			if (CSVHeaderSniffer.getMaxDigits(firstRowSignature) == 'D') {
				this.header = Collections.emptyList(); 
				return ;
			}

			char[] remainingRowsSignature = this
					.getRemainingRowsSignature(iterator, firstRowSize);

			for (int col = 0; col < firstRowSize; col++) {
				if (firstRowSignature[col] == '?'
						&& remainingRowsSignature[col] != '?') {
					this.header = new ArrayList<String>(firstRowSize);
					for (String s : firstRowRecord)
						this.header.add(s);
					return;
				}
			}
		}
	}

	protected char[] getRemainingRowsSignature(Iterator<CSVRecord> iterator,
			int firstRowSize) {
		char[] remainingRowsSignature = new char[firstRowSize];
		int[] digitsInColumn = new int[firstRowSize];

		int rows = 0;
		while (iterator.hasNext()) {
			rows++;
			CSVRecord record = iterator.next();

			char[] rowSignature = CSVHeaderSniffer.getSignature(record, firstRowSize);
			for (int col = 0; col < firstRowSize; col++) {
				if (rowSignature[col] != '?')
					digitsInColumn[col]++;
			}
		}
		
		for (int col = 0; col < firstRowSize; col++) {
			if (digitsInColumn[col] > 0.9 * rows)
				remainingRowsSignature[col] = 'd';
			else
				remainingRowsSignature[col] = '?';
		}
		return remainingRowsSignature;
	}

	private static char getMaxDigits(char[] firstRowSignature) {
		char d = '?';
		for (char c : firstRowSignature) {
			if (c == 'D')
				return 'D';
			else if (c == 'd')
				d = 'd';
		}
		return d;
	}

	private static char[] getSignature(CSVRecord record, int firstRowSize) {
		char[] signature = new char[firstRowSize];
		for (int col = 0; col < firstRowSize; col++) {
			if (col < record.size()) {
				String s = record.get(col);
				signature[col] = CSVHeaderSniffer.getType(s);
			} else
				signature[col] = '?';
		}
		return signature;
	}

	/**
	 * @param s
	 * @return 'd' (digit) if more than 80 % of the chars are digits, and there
	 *         is at least one digit.
	 */
	private static char getType(String s) {
		int digits = 0;
		int letters = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isLetter(c))
				letters++;
			else if (Character.isDigit(c))
				digits++;
			// else ignore
		}
		if (digits > 0) {
			if (letters == 0)
				return 'D';
			else if (digits >= 4 * letters)
				return 'd';
		}

		return '?';
	}

	public List<String> getHeader() {
		return this.header;
	}
}
