package com.github.jferard.csvsniffer;

/**
 * Begin of line state. Main difference with the BOF state: there is no previous delimiter.
 */
public class BOLState implements State {
    private boolean wasSpace;

    @Override
    public void handle(final Context context, final char c) {
        if (context.isQuote(c)) {
            context.storeQuote(c, this.wasSpace);
            context.setState(new InQuotedFieldState(-1, c));
        } else if (context.isSimpleSpace(c)) {
            this.wasSpace = true;
        } else if (context.isEOL(c)) {
            context.setState(new EOLState(c));
            this.wasSpace = false;
        } else if (context.isTraced(c)) {
            context.storeSeen(c);
            this.wasSpace = false;
        }
    }
}
