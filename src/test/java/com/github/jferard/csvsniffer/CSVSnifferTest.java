package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CSVSnifferTest {
    @Test
    public void test() throws IOException {
        final CSVSniffer sniffer = CSVSniffer.create();
        final ExtendedCSVData extended = sniffer.sniff(new ByteArrayInputStream(
                "col1;col2;col3\n\"1\";foo;bar\n\"2\";baz;bat\n\"3\";ç'est;\"une \"\"idée\"\"  intéressante\"".getBytes("UTF-8")));
        Assert.assertEquals("UTF-8", extended.getCharset());
        Assert.assertEquals(';', extended.getDelimiter());
        Assert.assertEquals('"', extended.getQuoteChar());
        Assert.assertEquals('\0', extended.getEscapeChar());
        Assert.assertTrue(extended.isDoubleQuote());
        Assert.assertFalse(extended.isSkipInitialSpace());
    }

    @Test
    public void test2() throws IOException {
        final CSVSniffer sniffer = CSVSniffer.create();
        final ExtendedCSVData extended = sniffer.sniff(new ByteArrayInputStream(
                "col1;col2;col3\n\"1\";foo;bar\n\"2\";baz;bat\n\"3\";ç'est;\"une \\\"idée\\\" intéressante\"".getBytes("UTF-8")));
        Assert.assertEquals("UTF-8", extended.getCharset());
        Assert.assertEquals(';', extended.getDelimiter());
        Assert.assertEquals('"', extended.getQuoteChar());
        Assert.assertEquals('\\', extended.getEscapeChar());
        Assert.assertFalse(extended.isDoubleQuote());
        Assert.assertFalse(extended.isSkipInitialSpace());
    }
}