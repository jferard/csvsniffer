package com.github.jferard.csvsniffer;

/**
 * A quote after a quote: "....\"
 */
public class MaybeEscapedQuoteState implements State {
    private final int expectedDelimiter;
    private final char quote;
    private final char escape;
    private boolean wasSpace;

    public MaybeEscapedQuoteState(int expectedDelimiter, char quote, char escape) {
        this.expectedDelimiter = expectedDelimiter;
        this.quote = quote;
        this.escape = escape;
    }

    @Override
    public void handle(Context context, char c) {
        if (context.isSimpleSpace(c)) {
            this.wasSpace = true;
        } else if (c == this.expectedDelimiter) { // assume it was not escaped
            context.storeDelimiter(c, this.wasSpace);
            context.setState(new BOFState(this.expectedDelimiter));
        } else {
            context.storeEscape(this.escape);
            context.setState(new InQuotedFieldState(this.expectedDelimiter, this.quote));
        }
    }
}
