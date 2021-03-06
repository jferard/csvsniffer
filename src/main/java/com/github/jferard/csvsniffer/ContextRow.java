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

    public void storeQuote(char c, boolean wasSpace) {
        if (wasSpace) {
            this.skipInitialSpaces += 1;
        }
        this.quotes.add(c);
    }

    public void storeDoubleQuote(char c) {
        this.doubleQuotes.add(c);
    }

    public void storeSeen(char c) {
        this.seen.add(c);
    }

    public void storeEol(String s) {
        this.eols.add(s);
    }

    public void storeEscape(char c) {
        this.escapes.add(c);
    }

    public void storeDelimiter(char c, boolean wasSpace) {
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

    public void aggregate(ContextAggregator contextAggregator) {
        contextAggregator.addQuotes(this.quotes);
        contextAggregator.addSeens(this.seen, this.delimiters);
        contextAggregator.addEols(this.eols);
        contextAggregator.addDoubleQuotes(this.doubleQuotes);
        contextAggregator.addEscapes(this.escapes);
        contextAggregator.addDelimiters(this.delimiters);
        contextAggregator.addSkipInitialSpaces(this.skipInitialSpaces);
    }
}
