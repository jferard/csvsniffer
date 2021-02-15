package com.github.jferard.csvsniffer;

public class QuoteInQuotedFieldState implements State {
    private int expectedDelimiter;
    private boolean wasSpace;

    public QuoteInQuotedFieldState(int expectedDelimiter) {
        this.expectedDelimiter = expectedDelimiter;
    }

    @Override
    public void handle(Context context, char c) {
        if (context.isQuote(c)) {
            context.storeDoubleQuote(c);
            context.setState(new InQuotedFieldState(this.expectedDelimiter));
        } else if (context.isSimpleSpace(c)) {
            this.wasSpace = true;
        } else if (c == this.expectedDelimiter) {
            assert this.expectedDelimiter != -1;
            context.storeQuote((char) context.prev(), wasSpace);
            context.storeDelimiter((char) this.expectedDelimiter, this.wasSpace);
            context.setState(new BOFState(this.expectedDelimiter));
        } else if (expectedDelimiter == -1 && context.isTraced(c)) {
            context.storeQuote((char) context.prev(), wasSpace);
            context.storeDelimiter(c, this.wasSpace);
            context.setState(new BOFState(c));
        } else if (context.isEOL(c)) {
            context.setState(new EOLState(c));
        } else {
            char escape = (char) context.prev();
            if (context.isTraced(escape)) {
                context.setState(new MaybeEscapedQuoteState(escape, this.expectedDelimiter));
            }
        }

    }
}
