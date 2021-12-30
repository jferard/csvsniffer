package com.github.jferard.csvsniffer.it;

import com.github.jferard.csvsniffer.MetaCSVSniffer;
import com.github.jferard.javamcsv.MetaCSVData;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MetaCSVSnifferIT {
    @Test
    public void test() throws IOException {
        for (final String name : new String[]{"example.csv", "wikipedia.csv",
                "export-discussion-20210213-064944.csv", "export-tag-20210213-065003.csv",
                "20201001-bal-216402149.csv", "datasets-2020-02-22-12-33.csv"}) {
            System.out.println(name);
            this.testResource(name);
        }
    }

    @Test
    public void testOne() throws IOException {
        for (final String name : new String[]{"20201001-bal-216402149.csv"}) {
            System.out.println(name);
            this.testResource(name);
        }
    }

    private void testResource(final String name) throws IOException {
        final InputStream is =
                MetaCSVSnifferIT.class.getClassLoader().getResourceAsStream(
                        name);
        final MetaCSVSniffer csvSniffer = MetaCSVSniffer.create();
        final MetaCSVData sniff = csvSniffer.sniff(is);
        System.out.println(sniff);
    }
}