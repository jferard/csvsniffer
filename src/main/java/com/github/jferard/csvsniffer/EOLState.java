package com.github.jferard.csvsniffer;

/**
 * CR or LF was found.
 */
public class EOLState implements State {
    private final char eolChar;

    public EOLState(char eolChar) {
        this.eolChar = eolChar;
    }

    @Override
    public void handle(Context context, char c) {
        if (context.isEOL(c)) {
            if (c == this.eolChar) { // LFLF or CRCR
                context.storeEol(""+this.eolChar);
                context.storeEol(""+c);
            } else { // CRLF or LFCR
                context.storeEol(""+this.eolChar +c);
            }
        } else {
            context.storeEol(""+this.eolChar);
            context.unget(c);
        }
        context.newRow();
    }
}
