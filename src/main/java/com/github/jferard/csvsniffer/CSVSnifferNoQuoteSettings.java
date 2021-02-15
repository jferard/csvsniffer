package com.github.jferard.csvsniffer;

public class CSVSnifferNoQuoteSettings implements CSVSnifferSettings {
    @Override
    public boolean isQuote(char c) {
        return false;
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
        return c == '\r' || c == '\n';
    }
}
