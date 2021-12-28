package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

public class ContextTest {
    public void test() {
        Context c = new Context(new CSVSnifferQuoteSettings('"'));
        c.handle(' ');
        c.handle('"');
        c.handle('a');
        c.handle('"');
        c.handle('"');
        c.handle('b');
        c.handle('"');
        c.handle(';');
        c.handle('"');
        c.handle('c');
        c.handle('"');
        c.handle('\n');
        c.handle('"');
        c.handle('e');
        c.handle('"');
        c.handle(';');
        c.handle('"');
        c.handle('f');
        c.handle('"');
        c.handle('\n');
        CSVData data = c.evaluate();
        Assert.assertEquals('\0', data.getEscapeChar());
        Assert.assertEquals(';', data.getDelimiter());
        Assert.assertEquals("\n", data.getLineTerminator());
        Assert.assertTrue(data.isDoubleQuote());
        Assert.assertTrue(data.isSkipInitialSpace());
    }

}