package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.MetaCSVData;

import java.util.HashMap;
import java.util.Map;

public class ContextRow {
    private Counter<Character> quotes;
    private Counter<Character> seen;
    private Counter<String> eols;
    private Counter<Character> doubleQuotes;
    private Counter<Character> escapes;
    private Counter<Character> delimiters;

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
                "quotes=" + quotes +
                ", seen=" + seen +
                ", eols=" + eols +
                ", doubleQuotes=" + doubleQuotes +
                ", escapes=" + escapes +
                ", delimiters=" + delimiters +
                ", skipInitialSpaces=" + skipInitialSpaces +
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
