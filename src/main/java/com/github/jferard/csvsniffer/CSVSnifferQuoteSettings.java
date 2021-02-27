package com.github.jferard.csvsniffer;

public class CSVSnifferQuoteSettings implements CSVSnifferSettings {
    private final char quote;

    public CSVSnifferQuoteSettings(char quote) {
        this.quote = quote;
    }

    @Override
    public boolean isQuote(char c) {
        return c == this.quote;
    }

    @Override
    public boolean isSimpleSpace(char c) {
        return c == ' ';
    }

    @Override
    public boolean isToIgnore(char c) {
        return Character.isLetterOrDigit(c);
    }

    @Override
    public boolean isEOL(char c) {
        return c == '\n' || c == '\r';
    }
}
