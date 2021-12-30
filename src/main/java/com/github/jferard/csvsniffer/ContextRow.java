package com.github.jferard.csvsniffer;

public class ContextRow {
    private final Counter<Character> quotes;
    private final Counter<Character> seen;
    private final Counter<String> eols;
    private final Counter<Character> doubleQuotes;
    private final Counter<Character> escapes;
    private final Counter<Character> delimiters;

    private int skipInitialSpaces;

    public ContextRow() {
        this.quotes = new Counter<Character>();
        this.seen = new Counter<Character>();
        this.eols = new Counter<String>();
        this.doubleQuotes = new Counter<Character>();
        this.escapes = new Counter<Character>();
        this.delimiters = new Counter<Character>();
    }

    public void storeQuote(final char c, final boolean wasSpace) {
        if (wasSpace) {
            this.skipInitialSpaces += 1;
        }
        this.quotes.add(c);
    }

    /**
     * A double quote was found inside a quoted string
     *
     * @param c the quote
     */
    public void storeDoubleQuote(final char c) {
        this.doubleQuotes.add(c);
    }

    /**
     * Store a char as "seen"
     *
     * @param c the char
     */
    public void storeSeen(final char c) {
        this.seen.add(c);
    }

    /**
     * A new EOL string was found
     *
     * @param eol the end of line sequence (CR, LF, CRLF)
     */
    public void storeEol(final String eol) {
        this.eols.add(eol);
    }

    public void storeEscape(final char c) {
        this.escapes.add(c);
    }

    public void storeDelimiter(final char c, final boolean wasSpace) {
        if (wasSpace) {
            this.skipInitialSpaces += 1;
        }
        this.delimiters.add(c);
    }

    @Override
    public String toString() {
        return "ContextRow{" +
                "quotes=" + this.quotes +
                ", seen=" + this.seen +
                ", eols=" + this.eols +
                ", doubleQuotes=" + this.doubleQuotes +
                ", escapes=" + this.escapes +
                ", delimiters=" + this.delimiters +
                ", skipInitialSpaces=" + this.skipInitialSpaces +
                '}';
    }

    public void aggregate(final ContextAggregator contextAggregator) {
        contextAggregator.newRow();
        contextAggregator.addQuotes(this.quotes);
        contextAggregator.addSeens(this.seen, this.delimiters);
        contextAggregator.addEols(this.eols);
        contextAggregator.addDoubleQuotes(this.doubleQuotes);
        contextAggregator.addEscapes(this.escapes);
        contextAggregator.addDelimiters(this.delimiters);
        contextAggregator.addSkipInitialSpaces(this.skipInitialSpaces);
    }

    /**
     * @return true if this counter is empty.
     */
    public boolean isEmpty() {
        return this.quotes.isEmpty() && this.seen.isEmpty() && this.eols.isEmpty() &&
                this.doubleQuotes.isEmpty() && this.escapes.isEmpty() && this.delimiters.isEmpty();
    }
}
