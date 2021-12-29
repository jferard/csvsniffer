package com.github.jferard.csvsniffer;

/**
 * A second quote was found: "....".
 */
public class QuoteInQuotedFieldState implements State {
    private final int expectedDelimiter;
    private final char quote;
    private final int prev;
    private boolean wasSpace;

    public QuoteInQuotedFieldState(final int expectedDelimiter, final char quote, final int prev) {
        this.expectedDelimiter = expectedDelimiter;
        this.quote = quote;
        this.prev = prev;
    }

    @Override
    public void handle(final Context context, final char c) {
        if (c == this.quote) { // "...""
            context.storeDoubleQuote(c);
            context.setState(new InQuotedFieldState(this.expectedDelimiter, c));
        } else if (context.isSimpleSpace(c)) {
            this.wasSpace = true;
        } else if (c == this.expectedDelimiter || (this.expectedDelimiter == -1 && context.isTraced(c))) { // "...";
            context.storeQuote((char) context.prev(), this.wasSpace);
            context.storeDelimiter(c, this.wasSpace);
            context.setState(new BOFState(c));
        } else if (context.isEOL(c)) {
            context.setState(new EOLState(c));
        } else {
            if (context.isTraced((char) this.prev)) {
                context.storeEscape((char) this.prev);
//                context.setState(new MaybeEscapedQuoteState(this.expectedDelimiter, this.quote,
//                        (char) this.prev));
            }
            context.setState(new InQuotedFieldState(this.expectedDelimiter, this.quote));
        }

    }
}
