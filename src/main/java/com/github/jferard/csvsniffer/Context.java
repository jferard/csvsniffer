package com.github.jferard.csvsniffer;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private ContextRow row;
    private CSVSnifferSettings settings;
    private int unget;
    private int prev;
    private State state;
    private List<ContextRow> rows;

    public Context(CSVSnifferSettings settings) {
        this.settings = settings;
        this.unget = -1;
        this.prev = -1;
        this.rows = new ArrayList<ContextRow>();
        this.newRow();
    }

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

    public void setState(State newState) {
        this.state = newState;
    }

    public boolean isQuote(char c) {
        return settings.isQuote(c);
    }

    public boolean isSimpleSpace(char c) {
        return settings.isSimpleSpace(c);
    }

    public boolean isEOL(char c) {
        return settings.isEOL(c);
    }

    public boolean isTraced(char c) {
        return !settings.isToIgnore(c);
    }

    public void unget(char c) {
        this.unget = c;
    }

    public int prev() {
        return this.prev;
    }

    public void storeQuote(char c, boolean wasSpace) {
        this.row.storeQuote(c, wasSpace);
    }

    public void storeDoubleQuote(char c) {
        this.row.storeDoubleQuote(c);
    }

    public void storeSeen(char c) {
        this.row.storeSeen(c);
    }

    public void storeEol(String s) {
        this.row.storeEol(s);
    }

    public void storeEscape(char prev) {
        this.row.storeEscape(prev);
    }

    public void storeDelimiter(char expectedDelimiter, boolean wasSpace) {
        this.row.storeDelimiter(expectedDelimiter, wasSpace);
    }

    public void newRow() {
        this.setState(new BOLState());
        this.row = new ContextRow();
        this.rows.add(row);
    }

    public void print() {
        System.out.println(this.rows);
    }

    public CSVData evaluate() {
        ContextAggregator contextAggregator = new ContextAggregator();
        for (ContextRow row : rows.subList(0, rows.size()-1)) {
            row.aggregate(contextAggregator);
        }
        return contextAggregator.aggregate();
    }
}
