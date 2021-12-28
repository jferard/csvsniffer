package com.github.jferard.csvsniffer;

/**
 * Begin of field
 */
public class BOFState implements State {
    private final int previousDelimiter;

    public BOFState(int previousDelimiter) {
        this.previousDelimiter = previousDelimiter;
    }

    private boolean wasSpace;

    @Override
    public void handle(Context context, char c) {
        if (context.isQuote(c)) {
            context.storeQuote(c, this.wasSpace);
            context.setState(new InQuotedFieldState(this.previousDelimiter, c));
        } else if (context.isSimpleSpace(c)) {
            this.wasSpace = true;
        } else if (context.isEOL(c)) {
            context.setState(new EOLState(c));
        } else if (context.isTraced(c)) {
            context.storeSeen(c);
            this.wasSpace = false;
        }
    }
}
