package com.github.jferard.csvsniffer;

public class InQuotedFieldState implements State {
    private int expectedDelimiter;

    public InQuotedFieldState(int expectedDelimiter) {
        this.expectedDelimiter = expectedDelimiter;
    }

    @Override
    public void handle(Context context, char c) {
        if (context.isQuote(c)) {
            context.setState(new QuoteInQuotedFieldState(this.expectedDelimiter));
        }
    }
}
