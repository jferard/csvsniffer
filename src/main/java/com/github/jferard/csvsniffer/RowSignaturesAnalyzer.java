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

import java.util.Iterator;

import org.apache.commons.csv.CSVRecord;

class RowSignaturesAnalyzer {
	public char[] getSignature(final CSVRecord record, final int firstRowSize) {
		final char[] signature = new char[firstRowSize];
		for (int col = 0; col < firstRowSize; col++) {
			if (col < record.size()) {
				final String s = record.get(col);
				signature[col] = this.getType(s);
			} else
				signature[col] = '?';
		}
		return signature;
	}

	/**
	 * @param s
	 * @return 'd' (digit) if more than 80 % of the chars are digits, and there
	 *         is at least one digit, 'D' if there are only digits, and '?' else
	 */
	/**
	 * @param s
	 * @return the type of the value : 'D' for digits only value, 'd' for 80%
	 *         digits values and at least one char, '?' else
	 */
	public char getType(final String s) {
		int digits = 0;
		int letters = 0;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
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

	/**
	 * @param iterator
	 * @param firstRowSize
	 * @return the signature of the remaining rows. Similar to the signature of
	 *         the first row, but global. 'd' if there is at least 90% of non
	 *         '?' values, '?' else
	 */
	public char[] getRemainingRowsSignature(final Iterator<CSVRecord> iterator,
			final int firstRowSize) {
		final char[] remainingRowsSignature = new char[firstRowSize];
		final int[] digitsInColumn = new int[firstRowSize];

		int rows = 0;
		// count the number of rows with digits in each column
		while (iterator.hasNext()) {
			rows++;
			final CSVRecord record = iterator.next();

			final char[] rowSignature = this.getSignature(record, firstRowSize);
			for (int col = 0; col < firstRowSize; col++) {
				if (rowSignature[col] != '?')
					digitsInColumn[col]++;
			}
		}

		// 90 % of digit in a column => a digit column
		for (int col = 0; col < firstRowSize; col++) {
			if (digitsInColumn[col] > 0.9 * rows)
				remainingRowsSignature[col] = 'd';
			else
				remainingRowsSignature[col] = '?';
		}
		return remainingRowsSignature;
	}
}
