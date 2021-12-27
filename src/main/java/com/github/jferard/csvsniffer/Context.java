package com.github.jferard.csvsniffer;

import java.util.ArrayList;
import java.util.List;

/**
 * A context in a CSVSniffer.
 */
public class Context {
    private final CSVSnifferSettings settings;
    private final List<ContextRow> rows;
    private ContextRow row;
    private int unget;
    private int prev;
    private State state;

    public Context(CSVSnifferSettings settings) {
        this.settings = settings;
        this.unget = -1;
        this.prev = -1;
        this.rows = new ArrayList<ContextRow>();
        this.newRow();
    }

    /**
     * Handle the next char
     *
     * @param c the char
     */
    public void handle(char c) {
        if (this.unget != -1) {
            char prev = (char) this.unget;
            this.unget = -1;
            this.state.handle(this, prev);
            if (!this.isSimpleSpace(prev)) {
                this.prev = prev;
            }
        }
        this.state.handle(this, c);
        if (!this.isSimpleSpace(c)) {
            this.prev = c;
        }
    }

    /**
     * Set the new state
     *
     * @param newState the state
     */
    public void setState(State newState) {
        this.state = newState;
    }

    /**
     * @param c the char
     * @return true if c is a quote
     */
    public boolean isQuote(char c) {
        return this.settings.isQuote(c);
    }

    /**
     * @param c the char
     * @return true if c is a space but not an EOL
     */
    public boolean isSimpleSpace(char c) {
        return this.settings.isSimpleSpace(c);
    }

    /**
     * @param c the char
     * @return true if c is an EOL
     */
    public boolean isEOL(char c) {
        return this.settings.isEOL(c);
    }

    /**
     * @param c the char
     * @return true if c may be part of the format (delimiter or quote)
     */
    public boolean isTraced(char c) {
        return !this.settings.isToIgnore(c);
    }

    /**
     * Unget the char
     *
     * @param c the char
     */
    public void unget(char c) {
        this.unget = c;
    }

    /**
     * @return the previous char (may be altered by unget)
     */
    public int prev() {
        return this.prev;
    }

    public void storeQuote(char c, boolean wasSpace) {
        this.row.storeQuote(c, wasSpace);
    }

    public void storeDoubleQuote(char c) {
        this.row.storeDoubleQuote(c);
    }

    /**
     * Store a char as "seen"
     *
     * @param c the char
     */
    public void storeSeen(char c) {
        this.row.storeSeen(c);
    }

    /**
     * A new EOL string was found
     * @param eol the end of line sequence (CR, LF, CRLF)
     */
    public void storeEol(String eol) {
        this.row.storeEol(eol);
    }

    public void storeEscape(char prev) {
        this.row.storeEscape(prev);
    }

    public void storeDelimiter(char expectedDelimiter, boolean wasSpace) {
        this.row.storeDelimiter(expectedDelimiter, wasSpace);
    }

    /**
     * A new row has started
     */
    public void newRow() {
        this.setState(new BOLState());
        this.row = new ContextRow();
        this.rows.add(this.row);
    }

    /**
     * Print the rows
     */
    public void print() {
        System.out.println(this.rows);
    }

    public CSVData evaluate() {
        ContextAggregator contextAggregator = new ContextAggregator();
        for (ContextRow row : this.rows.subList(0, this.rows.size() - 1)) {
            row.aggregate(contextAggregator);
        }
        return contextAggregator.aggregate();
    }
}
