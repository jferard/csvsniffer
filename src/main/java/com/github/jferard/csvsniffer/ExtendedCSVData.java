package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.CSVParameters;

public class ExtendedCSVData implements CSVParameters {
    private final String charset;
    private final CSVData data;

    public ExtendedCSVData(final String charset, final CSVData data) {
        this.charset = charset;
        this.data = data;
    }

    @Override
    public char getDelimiter() {
        return this.data.getDelimiter();
    }

    @Override
    public char getQuoteChar() {
        return this.data.getQuoteChar();
    }

    @Override
    public String getLineTerminator() {
        return this.data.getLineTerminator();
    }

    @Override
    public boolean isDoubleQuote() {
        return this.data.isDoubleQuote();
    }

    @Override
    public char getEscapeChar() {
        return this.data.getEscapeChar();
    }

    @Override
    public boolean isSkipInitialSpace() {
        return this.data.isSkipInitialSpace();
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return this.charset;
    }

}
