package com.github.jferard.csvsniffer;

public class EOLState implements State {
    private final char c;

    public EOLState(char c) {
        this.c = c;
    }

    @Override
    public void handle(Context context, char c) {
        if (context.isEOL(c)) {
            if (c == this.c) { // LFLF or CRCR
                context.storeEol(""+this.c);
                context.storeEol(""+c);
            } else { // CRLF or LFCR
                context.storeEol(""+this.c+c);
            }
        } else {
            context.unget(c);
        }
        context.newRow();
    }
}
