package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ContextAggregatorTest {
    @Test
    public void testAggregate1() {
        final ContextAggregator agg = new ContextAggregator();
        agg.newRow();
        agg.addQuotes(TestHelper.getCounter('"', 30));
        agg.addEols(TestHelper.getCounter("\r\n", 1));
        agg.addEscapes(TestHelper.getCounter('\\', 3));
        agg.addSkipInitialSpaces(3);
        agg.addSeens(TestHelper.getCounter(Arrays.asList(',','?', '!')),
                TestHelper.getCounter(',', 20));
        agg.addDelimiters(TestHelper.getCounter(',', 20));

        agg.newRow();
        agg.addQuotes(TestHelper.getCounter('\'', 3));
        agg.addEols(TestHelper.getCounter("\n", 10));
        agg.addDoubleQuotes(TestHelper.getCounter('"', 1));
        agg.addSkipInitialSpaces(7);
        agg.addSeens(TestHelper.getCounter(Arrays.asList(',','?', '!')),
                TestHelper.getCounter(',', 20));
        agg.addDelimiters(TestHelper.getCounter(',', 20));

        final CSVData data = agg.aggregate();
        Assert.assertEquals('\\', data.getEscapeChar());
        Assert.assertEquals("\n", data.getLineTerminator());
        Assert.assertEquals(',', data.getDelimiter());
        Assert.assertEquals('"', data.getQuoteChar());
    }

    @Test
    public void testAggregate2() {
        final ContextAggregator agg = new ContextAggregator();
        agg.newRow();
        agg.addQuotes(TestHelper.getCounter('"', 30));
        agg.addDoubleQuotes(TestHelper.getCounter('"', 1));
        agg.addSkipInitialSpaces(3);
        agg.addEols(TestHelper.getCounter("\r\n", 1));
        agg.addSeens(TestHelper.getCounter(',', 20),
                TestHelper.getCounter(',', 20));
        agg.addDelimiters(TestHelper.getCounter(',', 20));

        agg.newRow();
        agg.addQuotes(TestHelper.getCounter('\'', 3));
        agg.addEscapes(TestHelper.getCounter('\\', 3));
        agg.addSkipInitialSpaces(7);
        agg.addEols(TestHelper.getCounter("\n", 10));
        agg.addSeens(TestHelper.getCounter('|', 17),
                TestHelper.getCounter('|', 17));
        agg.addDelimiters(TestHelper.getCounter('|', 17));

        agg.newRow();
        agg.addSeens(TestHelper.getCounter(';', 15),
                TestHelper.getCounter(';', 15));
        agg.addDelimiters(TestHelper.getCounter(';', 15));
        final CSVData data = agg.aggregate();
        Assert.assertEquals('\0', data.getEscapeChar());
        Assert.assertEquals("\n", data.getLineTerminator());
        Assert.assertEquals(';', data.getDelimiter());
        Assert.assertEquals('\0', data.getQuoteChar());
    }

    @Test
    public void testToString() {
        final ContextAggregator agg = new ContextAggregator();
        agg.addQuotes(TestHelper.getCounter('"', 30));
        agg.addQuotes(TestHelper.getCounter('\'', 3));
        agg.addEols(TestHelper.getCounter("\r\n", 1));
        agg.addEols(TestHelper.getCounter("\n", 10));
        agg.addDoubleQuotes(TestHelper.getCounter('"', 1));
        agg.addEscapes(TestHelper.getCounter('\\', 3));
        agg.addSkipInitialSpaces(3);
        agg.addSkipInitialSpaces(7);
        agg.addSeens(TestHelper.getCounter(Arrays.asList(',','?', '!')),
                TestHelper.getCounter(',', 20));
        agg.addDelimiters(TestHelper.getCounter(',', 20));
        Assert.assertEquals("ContextAggregator{" +
                "quotes=Counter{countByElement={\"=30, '=3}}, " +
                "seen=MultiCounter{countsByElement={!=[1], ,=[21], ?=[1]}}, " +
                "eols=Counter{countByElement={\n=10, \r\n=1}}, " +
                "doubleQuotes=Counter{countByElement={\"=1}}, " +
                "escapes=Counter{countByElement={\\=3}}, " +
                "delimiters=Counter{countByElement={,=20}}, " +
                "skipInitialSpaces=10}", agg.toString());
    }
}