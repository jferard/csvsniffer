package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

public class CSVSnifferNoQuoteSettingsTest {
    @Test
    public void test() {
        CSVSnifferNoQuoteSettings settings = new CSVSnifferNoQuoteSettings();
        Assert.assertTrue(settings.isSimpleSpace(' '));
        Assert.assertTrue(settings.isEOL('\r'));
        Assert.assertTrue(settings.isEOL('\n'));
        Assert.assertTrue(settings.isToIgnore('a'));

        Assert.assertFalse(settings.isQuote('"'));
        Assert.assertFalse(settings.isSimpleSpace('\t'));
        Assert.assertFalse(settings.isEOL('\t'));
        Assert.assertFalse(settings.isToIgnore('?'));
    }

}
