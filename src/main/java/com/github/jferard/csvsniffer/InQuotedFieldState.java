package com.github.jferard.csvsniffer;

/**
 * A quote was found. Continue until the next quote
 */
public class InQuotedFieldState implements State {
    private final int expectedDelimiter;
    private char quote;

    public InQuotedFieldState(int expectedDelimiter, char quote) {
        this.expectedDelimiter = expectedDelimiter;
        this.quote = quote;
    }

    @Override
    public void handle(Context context, char c) {
        if (c == this.quote) {
            context.setState(new QuoteInQuotedFieldState(this.expectedDelimiter, c));
        }
    }
}
