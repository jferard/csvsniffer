package com.github.jferard.csvsniffer;

/**
 * Settings for a sniffer without any quote char.
 */
public class CSVSnifferNoQuoteSettings implements CSVSnifferSettings {
    @Override
    public boolean isQuote(final char c) {
        return false;
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
        return c == '\r' || c == '\n';
    }
}
