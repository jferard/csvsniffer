package com.github.jferard.csvsniffer;

/**
 * A second quote was found: "....".
 */
public class QuoteInQuotedFieldState implements State {
    private final int expectedDelimiter;
    private final char quote;
    private boolean wasSpace;

    public QuoteInQuotedFieldState(int expectedDelimiter, char quote) {
        this.expectedDelimiter = expectedDelimiter;
        this.quote = quote;
    }

    @Override
    public void handle(Context context, char c) {
        if (c == this.quote) { // "...""
            context.storeDoubleQuote(c);
            context.setState(new InQuotedFieldState(this.expectedDelimiter, c));
        } else if (context.isSimpleSpace(c)) {
            this.wasSpace = true;
        } else if (c == this.expectedDelimiter || (this.expectedDelimiter == -1 && context.isTraced(c))) {
            context.storeQuote((char) context.prev(), this.wasSpace);
            context.storeDelimiter(c, this.wasSpace);
            context.setState(new BOFState(c));
        } else if (context.isEOL(c)) {
            context.setState(new EOLState(c));
        } else {
            char escape = (char) context.prev();
            if (context.isTraced(escape)) {
                context.setState(new MaybeEscapedQuoteState(this.expectedDelimiter, this.quote, escape));
            }
        }

    }
}
