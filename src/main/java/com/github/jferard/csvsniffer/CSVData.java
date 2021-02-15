package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.FieldDescription;
import com.github.jferard.javamcsv.MetaCSVData;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class CSVData {
    private final String eol;
    private final Character quote;
    private final boolean doubleQuote;
    private final Character escape;
    private final boolean skipInitialSpaces;
    private char delimiter;

    public CSVData(String eol, char delimiter, Character quote, boolean doubleQuote,
                   Character escape, boolean skipInitialSpaces) {
        this.eol = eol;
        this.delimiter = delimiter;
        this.quote = quote;
        this.doubleQuote = doubleQuote;
        this.escape = escape;
        this.skipInitialSpaces = skipInitialSpaces;
    }

    public MetaCSVData toMetaCSVData(String charset) {
        Map<String, String> meta = new HashMap<String, String>();
        meta.put("generator", "csvsniffer");
        Map<Integer, FieldDescription<?>> descriptionByColIndex =
                new HashMap<Integer, FieldDescription<?>>();
        return new MetaCSVData("draft0", meta, Charset.forName(charset), false, this.eol,
                this.delimiter, this.quote == null ? '\0' : this.quote, this.doubleQuote, this.escape == null ? '\0' : this.escape, this.skipInitialSpaces,
                "", descriptionByColIndex);
    }
}
