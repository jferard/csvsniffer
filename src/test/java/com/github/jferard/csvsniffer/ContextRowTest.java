package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class ContextRowTest {
    @Test
    public void testToString() {
        ContextRow c = new ContextRow();
        c.storeDelimiter(';', true);
        c.storeDoubleQuote('"');
        c.storeEol("\r\n");
        c.storeEscape('\\');
        c.storeQuote('"', true);
        c.storeSeen(',');
        Assert.assertEquals(
                "ContextRow{quotes=Counter{countByElement={\"=1}}, " +
                        "seen=Counter{countByElement={,=1}}, " +
                        "eols=Counter{countByElement={\r\n=1}}, " +
                        "doubleQuotes=Counter{countByElement={\"=1}}, " +
                        "escapes=Counter{countByElement={\\=1}}, " +
                        "delimiters=Counter{countByElement={;=1}}, skipInitialSpaces=2}",
                c.toString());
    }

    @Test
    public void testAgg() {
        ContextAggregator agg = PowerMock.createMock(ContextAggregator.class);
        ContextRow c = new ContextRow();
        c.storeDelimiter(';', true);
        c.storeDelimiter(';', false);
        c.storeDoubleQuote('"');
        c.storeEol("\r\n");
        c.storeEscape('\\');
        c.storeQuote('"', true);
        c.storeQuote('"', false);
        c.storeSeen(',');
        c.storeSeen(',');

        PowerMock.resetAll();
        agg.addQuotes(this.getCounter('"', 2));
        agg.addSeens(this.getCounter(',', 2), this.getCounter(';', 2));
        agg.addEols(this.getCounter("\r\n", 1));
        agg.addDoubleQuotes(this.getCounter('"', 1));
        agg.addEscapes(this.getCounter('\\', 1));
        agg.addDelimiters(this.getCounter(';', 2));
        agg.addSkipInitialSpaces(2);

        PowerMock.replayAll();
        c.aggregate(agg);

        PowerMock.verifyAll();
    }

    private <T> Counter<T> getCounter(T element, int n) {
        Counter<T> c = new Counter<T>();
        c.add(element, n);
        return c;
    }
}