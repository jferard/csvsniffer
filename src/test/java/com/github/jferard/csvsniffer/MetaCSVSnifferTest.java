package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.MetaCSVData;
import com.github.jferard.javamcsv.MetaCSVRenderer;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

public class MetaCSVSnifferTest {
    @Test
    public void test() throws IOException {
        final MetaCSVSniffer sniffer = MetaCSVSnifferFactory.create(Locale.FRANCE);
        final MetaCSVData data = sniffer.sniff(new ByteArrayInputStream(
                "col1;col2;col3\n\"1\";foo;bar\n\"2\";baz;bat\n\"3\";ç'est;\"une \"\"idée\"\"  intéressante\"".getBytes(
                        "UTF-8")));
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final MetaCSVRenderer renderer = MetaCSVRenderer.create(out, false);
        renderer.render(data);
        Assert.assertEquals("domain,key,value\r\n" +
                "file,encoding,UTF-8\r\n" +
                "file,bom,false\r\n" +
                "file,line_terminator,\\n\r\n" +
                "csv,delimiter,;\r\n" +
                "csv,double_quote,true\r\n" +
                "csv,quote_char,\"\"\"\"\r\n" +
                "csv,skip_initial_space,false\r\n" +
                "data,null_value,\r\n" +
                "data,col/0/type,integer\r\n", out.toString("UTF-8"));
    }
}