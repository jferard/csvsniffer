package com.github.jferard.csvsniffer.it;

import com.github.jferard.csvsniffer.MetaCSVSniffer;
import com.github.jferard.javamcsv.MetaCSVData;
import com.github.jferard.javamcsv.MetaCSVParseException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MetaCSVSnifferIT {
    @Test
    public void test() throws IOException, MetaCSVParseException {
        for (String name : new String[] {"example.csv", "wikipedia.csv", "export-discussion-20210213-064944.csv", "export-tag-20210213-065003.csv", "20201001-bal-216402149.csv"}) {
            System.out.println(name);
            testResource(name);
        }
    }

    private void testResource(String name) throws IOException, MetaCSVParseException {
        InputStream is =
                MetaCSVSnifferIT.class.getClassLoader().getResourceAsStream(
                        name);
        MetaCSVSniffer csvSniffer = MetaCSVSniffer.create();
        MetaCSVData sniff = csvSniffer.sniff(is);
        System.out.println(sniff);
    }
}