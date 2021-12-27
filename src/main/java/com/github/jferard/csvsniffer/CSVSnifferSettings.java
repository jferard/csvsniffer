package com.github.jferard.csvsniffer;

/**
 * Settings for a CSV sniffer.
 */
public interface CSVSnifferSettings {
    /**
     * @param c the char
     * @return true if c is a quote
     */
    boolean isQuote(char c);

    /**
     * @param c the char
     * @return true if c is a space but not an EOL
     */
    boolean isSimpleSpace(char c);

    /**
     * @param c the char
     * @return true if c is a char to ignore (eg. a letter or a digit is never a delimiter or a
     * quote char)
     */
    boolean isToIgnore(char c);

    /**
     * @param c the char
     * @return true if c is an EOL
     */
    boolean isEOL(char c);
}
