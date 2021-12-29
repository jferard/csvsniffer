package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.CSVParameters;
import com.github.jferard.javamcsv.description.FieldDescription;
import com.github.jferard.javamcsv.MetaCSVData;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class CSVData implements CSVParameters {
    private final String eol;
    private final Character quote;
    private final boolean doubleQuote;
    private final Character escape;
    private final boolean skipInitialSpaces;
    private final char delimiter;

    public CSVData(String eol, char delimiter, Character quote, boolean doubleQuote,
                   Character escape, boolean skipInitialSpaces) {
        this.eol = eol;
        this.delimiter = delimiter;
        this.quote = quote;
        this.doubleQuote = doubleQuote;
        this.escape = escape;
        this.skipInitialSpaces = skipInitialSpaces;
    }

    @Override
    public char getDelimiter() {
        return this.delimiter;
    }

    @Override
    public char getQuoteChar() {
        return this.quote == null ? '\0' : this.quote;
    }

    @Override
    public String getLineTerminator() {
        return this.eol;
    }

    @Override
    public boolean isDoubleQuote() {
        return this.doubleQuote;
    }

    @Override
    public char getEscapeChar() {
        return this.escape == null ? '\0' : this.escape;
    }

    @Override
    public boolean isSkipInitialSpace() {
        return this.skipInitialSpaces;
    }
}
