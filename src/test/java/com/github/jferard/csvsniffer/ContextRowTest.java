package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class ContextRowTest {
    @Test
    public void testToString() {
        final ContextRow c = new ContextRow();
        Assert.assertTrue(c.isEmpty());
        c.storeDelimiter(';', true);
        Assert.assertFalse(c.isEmpty());
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
        final ContextAggregator agg = PowerMock.createMock(ContextAggregator.class);
        final ContextRow c = new ContextRow();
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
        agg.newRow();
        agg.addQuotes(TestHelper.getCounter('"', 2));
        agg.addSeens(TestHelper.getCounter(',', 2), TestHelper.getCounter(';', 2));
        agg.addEols(TestHelper.getCounter("\r\n", 1));
        agg.addDoubleQuotes(TestHelper.getCounter('"', 1));
        agg.addEscapes(TestHelper.getCounter('\\', 1));
        agg.addDelimiters(TestHelper.getCounter(';', 2));
        agg.addSkipInitialSpaces(2);

        PowerMock.replayAll();
        c.aggregate(agg);

        PowerMock.verifyAll();
    }

}