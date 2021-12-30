package com.github.jferard.csvsniffer;

/**
 * A quote was found. Continue until the next quote
 */
public class InQuotedFieldState implements State {
    private final int expectedDelimiter;
    private final char quote;

    public InQuotedFieldState(final int expectedDelimiter, final char quote) {
        this.expectedDelimiter = expectedDelimiter;
        this.quote = quote;
    }

    @Override
    public void handle(final Context context, final char c) {
        if (c == this.quote) {
            context.setState(new QuoteInQuotedFieldState(this.expectedDelimiter, c, context.prev()));
        }
    }
}
