package com.github.jferard.csvsniffer;

/**
 * Begin of line state
 */
public class BOLState implements State {
    private boolean wasSpace;

    @Override
    public void handle(Context context, char c) {
        if (context.isQuote(c)) {
            context.storeQuote(c, wasSpace);
            context.setState(new InQuotedFieldState(-1));
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
