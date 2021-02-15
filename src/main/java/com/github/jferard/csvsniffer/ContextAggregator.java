package com.github.jferard.csvsniffer;

import java.util.Iterator;
import java.util.List;

public class ContextAggregator {
    private Counter<Character> quotes;
    private MultiCounter<Character> seen;
    private Counter<String> eols;
    private Counter<Character> doubleQuotes;
    private Counter<Character> escapes;
    private Counter<Character> delimiters;
    private int skipInitialSpaces;

    ContextAggregator() {
        this.quotes = new Counter<Character>();
        this.seen = new MultiCounter<Character>();
        this.eols = new Counter<String>();
        this.doubleQuotes = new Counter<Character>();
        this.escapes = new Counter<Character>();
        this.delimiters = new Counter<Character>();
    }

    public void addQuotes(Counter<Character> quotes) {
        this.quotes.update(quotes);
    }

    public void addEols(Counter<String> eols) {
        this.eols.update(eols);
    }

    public void addDoubleQuotes(Counter<Character> doubleQuotes) {
        this.doubleQuotes.update(doubleQuotes);
    }

    public void addEscapes(Counter<Character> escapes) {
        this.escapes.update(escapes);
    }

    public void addSkipInitialSpaces(int skipInitialSpaces) {
        this.skipInitialSpaces += skipInitialSpaces;
    }

    public void addSeens(Counter<Character> seen,
                         Counter<Character> delimiters) {
        Counter<Character> counter = new Counter<Character>(seen);
        counter.update(delimiters);
        this.seen.update(counter);
    }

    public void addDelimiters(Counter<Character> delimiters) {
        this.delimiters.update(delimiters);
    }

    public CSVData aggregate() {
        List<Character> dels = this.delimiters.descKeys();
        for (char delimiter : dels) {
            List<Integer> counts = this.seen.get(delimiter);
            Sequence sequence = new Sequence(counts);
            sequence.clean(10);
            float score = sequence.score();
            if (score < 0.01) {
                Character quote = this.quotes.firstOrNull();
                String eol = this.eols.firstOr("\r\n");
                Character escape = this.escapes.firstOrNull();
                boolean skipInitialSpaces = this.skipInitialSpaces > 0;
                Character doubleQuote = this.doubleQuotes.firstOrNull();
                return new CSVData(eol, delimiter, quote, doubleQuote == quote, escape, skipInitialSpaces);
            }
        }
        char bestChar = '\0';
        float best = 1000;
        for (char seen : this.seen.descKeys()) {
            List<Integer> counts = this.seen.get(seen);
            Sequence sequence = new Sequence(counts);
            sequence.clean(10);
            float score = sequence.score();
            if (score < best) {
                best = score;
                bestChar = seen;
                if (score < 0.01) {
                    break;
                }
            }
        }
        String eol = this.eols.firstOr("\r\n");
        return new CSVData(eol, bestChar, null, false, null, false);
    }

    @Override
    public String toString() {
        return "ContextAggregator{" +
                "quotes=" + quotes +
                ", seen=" + seen +
                ", eols=" + eols +
                ", doubleQuotes=" + doubleQuotes +
                ", escapes=" + escapes +
                ", delimiters=" + delimiters +
                ", skipInitialSpaces=" + skipInitialSpaces +
                '}';
    }
}
