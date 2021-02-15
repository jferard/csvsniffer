package com.github.jferard.csvsniffer;

public class MaybeEscapedQuoteState implements State {
    private final char escape;
    private final int expectedDelimiter;
    private boolean wasSpace;

    public MaybeEscapedQuoteState(char escape, int expectedDelimiter) {
        this.escape = escape;
        this.expectedDelimiter = expectedDelimiter;
    }

    @Override
    public void handle(Context context, char c) {
        if (context.isSimpleSpace(c)) {
            this.wasSpace = true;
        } else if (c == this.expectedDelimiter) {
            assert this.expectedDelimiter != -1;
            context.storeDelimiter(c, this.wasSpace);
            context.setState(new BOFState(this.expectedDelimiter));
        } else {
            context.storeEscape(this.escape);
            context.setState(new InQuotedFieldState(expectedDelimiter));
        }
    }
}
