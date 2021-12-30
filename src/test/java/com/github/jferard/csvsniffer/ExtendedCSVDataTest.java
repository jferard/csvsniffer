package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

public class ExtendedCSVDataTest {
    @Test
    public void test() {
        final CSVData data = new CSVData("\n", ',', '"', false, '\\', true);
        final ExtendedCSVData extended = new ExtendedCSVData("UTF-8", data);
        Assert.assertEquals("UTF-8", extended.getCharset());
        Assert.assertEquals("\n", extended.getLineTerminator());
        Assert.assertEquals(',', extended.getDelimiter());
        Assert.assertEquals('"', extended.getQuoteChar());
        Assert.assertFalse(extended.isDoubleQuote());
        Assert.assertEquals('\\', extended.getEscapeChar());
        Assert.assertTrue(extended.isSkipInitialSpace());
    }
}