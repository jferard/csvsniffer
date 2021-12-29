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

//    @Test
//    public void testToMeta() {
//        final CSVData data = new CSVData("\n", ',', '"', false, '\\', true);
//        final ExtendedCSVData extended = new ExtendedCSVData("UTF-8", data);
//        final MetaCSVData meta = extended.toMetaCSVData();
//        Assert.assertEquals(String.class, meta.getMetaData().getJavaType(0));
//        Assert.assertEquals("csvsniffer", meta.getMeta("generator"));
//        Assert.assertEquals("draft0", meta.getMetaVersion());
//        Assert.assertEquals(Charset.forName("UTF-8"), meta.getEncoding());
//        Assert.assertEquals("", meta.getNullValue());
//        Assert.assertEquals("\n", meta.getLineTerminator());
//        Assert.assertEquals(',', meta.getDelimiter());
//        Assert.assertEquals('"', meta.getQuoteChar());
//        Assert.assertFalse(meta.isDoubleQuote());
//        Assert.assertEquals('\\', meta.getEscapeChar());
//        Assert.assertTrue(meta.isSkipInitialSpace());
//        Assert.assertNull(meta.getDescription(0));
//    }
}