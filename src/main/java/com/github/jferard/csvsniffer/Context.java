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

    public Context(final CSVSnifferSettings settings) {
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
    public void handle(final char c) {
        if (this.unget != -1) {
            final char prev = (char) this.unget;
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
    public void setState(final State newState) {
        this.state = newState;
    }

    /**
     * @param c the char
     * @return true if c is a quote
     */
    public boolean isQuote(final char c) {
        return this.settings.isQuote(c);
    }

    /**
     * @param c the char
     * @return true if c is a space but not an EOL
     */
    public boolean isSimpleSpace(final char c) {
        return this.settings.isSimpleSpace(c);
    }

    /**
     * @param c the char
     * @return true if c is an EOL
     */
    public boolean isEOL(final char c) {
        return this.settings.isEOL(c);
    }

    /**
     * @param c the char
     * @return true if c may be part of the format (delimiter or quote)
     */
    public boolean isTraced(final char c) {
        return !this.settings.isToIgnore(c);
    }

    /**
     * Unget the char
     *
     * @param c the char
     */
    public void unget(final char c) {
        this.unget = c;
    }

    /**
     * @return the previous char (may be altered by unget)
     */
    public int prev() {
        return this.prev;
    }

    /**
     * @param c we assume that c is a quote char
     * @param wasSpace a space was met
     */
    public void storeQuote(final char c, final boolean wasSpace) {
        this.row.storeQuote(c, wasSpace);
    }

    /**
     * A double quote was found inside a quoted string
     * @param c the quote
     */
    public void storeDoubleQuote(final char c) {
        this.row.storeDoubleQuote(c);
    }

    /**
     * Store a char as "seen"
     *
     * @param c the char
     */
    public void storeSeen(final char c) {
        this.row.storeSeen(c);
    }

    /**
     * A new EOL string was found
     * @param eol the end of line sequence (CR, LF, CRLF)
     */
    public void storeEol(final String eol) {
        this.row.storeEol(eol);
    }

    public void storeEscape(final char prev) {
        this.row.storeEscape(prev);
    }

    public void storeDelimiter(final char expectedDelimiter, final boolean wasSpace) {
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

    /**
     * @return the CSV data.
     */
    public CSVData evaluate() {
        final ContextAggregator contextAggregator = new ContextAggregator();
        for (final ContextRow row : this.rows) {
            if (row.isEmpty()) {
                continue;
            }
            row.aggregate(contextAggregator);
        }
        return contextAggregator.aggregate();
    }
}
