package com.github.jferard.csvsniffer;

/**
 * Settings for a sniffer with a quote char (the quote char is expected)
 */
public class CSVSnifferQuoteSettings implements CSVSnifferSettings {
    private final char quote;

    public CSVSnifferQuoteSettings(final char quote) {
        this.quote = quote;
    }

    @Override
    public boolean isQuote(final char c) {
        return c == this.quote;
    }

    @Override
    public boolean isSimpleSpace(final char c) {
        return c == ' ';
    }

    @Override
    public boolean isToIgnore(final char c) {
        return Character.isLetterOrDigit(c);
    }

    @Override
    public boolean isEOL(final char c) {
        return c == '\n' || c == '\r';
    }
}
