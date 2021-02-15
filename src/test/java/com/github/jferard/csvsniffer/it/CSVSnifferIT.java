package com.github.jferard.csvsniffer.it;

import com.github.jferard.csvsniffer.CSVSniffer;
import com.github.jferard.csvsniffer.CSVSnifferNoQuoteSettings;
import com.github.jferard.csvsniffer.CSVSnifferQuoteSettings;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class CSVSnifferIT {
    @Test
    public void test() throws IOException {
        for (String name : new String[] {"example.csv", "wikipedia.csv"}) {
            System.out.println(name);
            testResource(name);
        }
    }

    private void testResource(String name) throws IOException {
        InputStream is =
                CSVSnifferIT.class.getClassLoader().getResourceAsStream(
                        name);
        CSVSniffer csvSniffer = new CSVSniffer(128*1024, new CSVSnifferQuoteSettings('"'),
                new CSVSnifferQuoteSettings('\''),
                new CSVSnifferNoQuoteSettings());
        System.out.println(csvSniffer.sniff(is));
    }
}