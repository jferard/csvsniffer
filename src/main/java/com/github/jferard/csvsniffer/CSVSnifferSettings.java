package com.github.jferard.csvsniffer;

public interface CSVSnifferSettings {
    boolean isQuote(char c);

    boolean isSimpleSpace(char c);

    boolean isToIgnore(char c);

    boolean isEOL(char c);
}
