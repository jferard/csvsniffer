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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

/**
 * The CSVOptionalHeaderSniffer class is a Sniffer that checks if the provided
 * stream contains a header. Here are the steps :
 * 
 * <ol>
 * <li>if the first row contains at least on digits only value, then there is no
 * header in the stream</li>
 * <li>if the first row contains at least on digits only value, then there is no
 * header in the stream</li>
 * </ol>
 *
 * @author Julien Férard (C) 2016
 *
 */
public class CSVOptionalHeaderSniffer implements Sniffer {
	public static CSVOptionalHeaderSniffer getSniffer(final byte delimiter,
			final byte quote, final byte escape, final Charset charset) {
		CSVFormat csvFormat = CSVFormat.newFormat((char) delimiter)
				.withQuote((char) quote).withQuoteMode(QuoteMode.MINIMAL)
				.withAllowMissingColumnNames();
		if (escape != quote)
			csvFormat = csvFormat.withEscape((char) escape);
		return new CSVOptionalHeaderSniffer(csvFormat, charset);
	}

	private final Charset charset;

	private final CSVFormat csvFormat;

	private List<String> header;

	private RowSignaturesAnalyzer rowSignaturesAnalyzer;

	public CSVOptionalHeaderSniffer(final CSVFormat csvFormat,
			final Charset charset) {
		this.charset = charset;
		this.csvFormat = csvFormat;
		this.rowSignaturesAnalyzer = new RowSignaturesAnalyzer();
	}

	public List<String> getHeader() {
		return this.header;
	}

	@Override
	public void sniff(final InputStream inputStream, final int size)
			throws IOException {
		final Reader streamReader = new InputStreamReader(inputStream,
				this.charset);

		final CSVParser parser = new CSVParser(streamReader, this.csvFormat);
		try {
			final Iterator<CSVRecord> iterator = parser.iterator();

			if (iterator.hasNext()) {
				final CSVRecord firstRowRecord = iterator.next();
				final int firstRowSize = firstRowRecord.size();

				final char[] firstRowSignature = this.rowSignaturesAnalyzer
						.getSignature(firstRowRecord, firstRowSize);

				if (this.containsAtLeastOneOnlyDigitsValue(firstRowSignature)) {
					this.header = null;
				} else {
					final char[] remainingRowsSignature = this.rowSignaturesAnalyzer
							.getRemainingRowsSignature(iterator, firstRowSize);
					if (this.containsAtLeastOneColumnWithLetterHeaderAndDigitValues(
							firstRowSignature, remainingRowsSignature,
							firstRowSize)) {
						// copy firstRow in header
						for (final String s : firstRowRecord)
							this.header.add(s);
					}
				}
			} else 
				this.header = null;
		} finally {
			parser.close();
		}
	}

	private boolean containsAtLeastOneColumnWithLetterHeaderAndDigitValues(
			final char[] firstRowSignature, final char[] remainingRowsSignature,
			final int firstRowSize) {
		for (int col = 0; col < firstRowSize; col++) {
			// at least one column with non digit first cell and digit next
			// cells
			if (firstRowSignature[col] == '?'
					&& remainingRowsSignature[col] != '?') {
				this.header = new ArrayList<String>(firstRowSize);
				return true;
			}
		}
		return false;
	}

	private boolean containsAtLeastOneOnlyDigitsValue(
			final char[] firstRowSignature) {
		for (final char c : firstRowSignature) {
			if (c == 'D')
				return true;
		}
		return false;
	}
}
