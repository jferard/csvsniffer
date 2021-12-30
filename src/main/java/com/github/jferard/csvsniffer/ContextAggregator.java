package com.github.jferard.csvsniffer;

import java.util.List;

/**
 * Aggregate the context.
 */
public class ContextAggregator {
    private final Counter<Character> quotes;
    private final MultiCounter<Character> seen;
    private final Counter<String> eols;
    private final Counter<Character> doubleQuotes;
    private final Counter<Character> escapes;
    private final Counter<Character> delimiters;
    private int skipInitialSpaces;
    private int rowCount;

    ContextAggregator() {
        this.quotes = new Counter<Character>();
        this.seen = new MultiCounter<Character>();
        this.eols = new Counter<String>();
        this.doubleQuotes = new Counter<Character>();
        this.escapes = new Counter<Character>();
        this.delimiters = new Counter<Character>();
        this.rowCount = 0;
    }

    /**
     * Add quotes from a context row
     * @param quotes the quotes
     */
    public void addQuotes(final Counter<Character> quotes) {
        this.quotes.update(quotes);
    }

    /**
     * Add eols from a context row
     * @param eols the eols
     */
    public void addEols(final Counter<String> eols) {
        this.eols.update(eols);
    }

    /**
     * Add doubleQuotes from a context row
     * @param doubleQuotes the quotes
     */
    public void addDoubleQuotes(final Counter<Character> doubleQuotes) {
        this.doubleQuotes.update(doubleQuotes);
    }

    /**
     * Add escapes from a context row
     * @param escapes the escapes
     */
    public void addEscapes(final Counter<Character> escapes) {
        this.escapes.update(escapes);
    }

    /**
     * Add the number of spaces skipped
     * @param skipInitialSpaces the spaces count
     */
    public void addSkipInitialSpaces(final int skipInitialSpaces) {
        this.skipInitialSpaces += skipInitialSpaces;
    }

    /**
     * Add the seen chars in a row. The delimiters are seen.
     * @param seen the chars
     * @param delimiters the delimiters
     */
    public void addSeens(final Counter<Character> seen,
                         final Counter<Character> delimiters) {
        final Counter<Character> counter = new Counter<Character>(seen);
        counter.update(delimiters);
        this.seen.update(counter);
    }

    /**
     * Add the delimiter chars in a row
     * @param delimiters the delimiters
     */
    public void addDelimiters(final Counter<Character> delimiters) {
        this.delimiters.update(delimiters);
    }

    /**
     * @return the aggregate CSVData
     */
    public CSVData aggregate() {
        final List<Character> dels = this.delimiters.descKeys();
        for (final char delimiter : dels) {
            final List<Integer> counts = this.seen.get(delimiter);
            final Sequence sequence = new Sequence(counts);
            sequence.clean(10);
            final double score = sequence.score(this.rowCount);
            if (score < 0.01) {
                final Character quote = this.quotes.firstOrNull();
                final String eol = this.eols.firstOr("\r\n");
                final Character escape = this.escapes.firstOrNull();
                final boolean skipInitialSpaces = this.skipInitialSpaces > 0;
                final Character doubleQuote = this.doubleQuotes.firstOrNull();
                return new CSVData(eol, delimiter, quote, doubleQuote == quote, escape, skipInitialSpaces);
            }
        }
        char bestChar = '\0';
        double best = 1000;
        for (final char seen : this.seen.descKeys()) {
            final List<Integer> counts = this.seen.get(seen);
            final Sequence sequence = new Sequence(counts);
            sequence.clean(10);
            final double score = sequence.score(this.rowCount);
            if (score < best) {
                best = score;
                bestChar = seen;
                if (score < 0.01) {
                    break;
                }
            }
        }
        final String eol = this.eols.firstOr("\r\n");
        return new CSVData(eol, bestChar, null, false, null, false);
    }

    @Override
    public String toString() {
        return "ContextAggregator{" +
                "quotes=" + this.quotes +
                ", seen=" + this.seen +
                ", eols=" + this.eols +
                ", doubleQuotes=" + this.doubleQuotes +
                ", escapes=" + this.escapes +
                ", delimiters=" + this.delimiters +
                ", skipInitialSpaces=" + this.skipInitialSpaces +
                '}';
    }

    /**
     * Declare a new row
     */
    public void newRow() {
        this.rowCount++;
    }
}
