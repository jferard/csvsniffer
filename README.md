# CSV Sniffer
(C) J. Férard 2016

A simple sniffer to detect file encoding and CSV format of a file, under GPL v3.

## Introduction
CSV Sniffer is a slow but (I hope) reliable sniffer that detects, for a given CSV file :
- its encoding, among three values : ASCII, UTF-8, "other" ;
- its delimiter char, quote char and escape char ;
- whether it has or not a header.

## Constraints on CSV format
### Mandatory constraints
* At least two columns ;
* delimiter, quote, escape are ASCII chars (0..127), but can't be letters, digits or space char.

### Optional constraints
It is possible to specifiy :
* the minimum number of columns allowed ;
* the set of chars accepted for delimiter, quote or escape.
Those optional constraints highly improve the reliability of the result.

## Process
Given a sample chunk of the file, CSV sniffer processes the data.

### Encoding
All bytes are processed. If all bytes are less than 128, then the encoding is expected to be ASCII. If a leading UTF-8 byte is found, but the trailing bytes are missing, encoding is "other". Else (all UTF-8 leading bytes have their trailing bytes), it is expected to be UTF-8.

### Delimiter
The chunk is split into lines on CR, LF or CRLF.
All lines are processed, splitted on all possible delimiter chars. The winner depends on mean and variance in thee set of lines.

### Quote
Each line is split on the winner delimiter. First and last char may be the quote char. The winner depends on the number of apparitions as first and last char, and (less) as first or last char. 

### Escape
The character which is more present before delimiter in really (by commons csv) parsed records.

### Header
If there is a full digit record in first line, then this line is not the header. Else, first and following lines must match.