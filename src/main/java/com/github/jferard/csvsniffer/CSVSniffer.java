package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.MetaCSVData;
import org.mozilla.intl.chardet.nsDetector;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVSniffer {
    public static CSVSniffer create() {
        return new CSVSniffer(128*1024, new CSVSnifferQuoteSettings('"'),
                new CSVSnifferQuoteSettings('\''),
                new CSVSnifferNoQuoteSettings());
    }

    private final CSVSnifferSettings[] settingsList;
    private String charset;
    final int bufferSize;

    public CSVSniffer(int bufferSize, CSVSnifferSettings... settingsList) {
        this.bufferSize = bufferSize;
        this.settingsList = settingsList;
        this.charset = null;
    }

    public MetaCSVData sniff(InputStream is) throws IOException {
        byte[] buffer = readToBuffer(is);
        charset = detectCharset(buffer);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buffer), charset));
        try {
            CSVData data = detectCSVParameters(reader);
            return data.toMetaCSVData(charset);
        } finally {
            reader.close(); // should not be necessary (byte array)
        }
    }

    public String detectCharset(byte[] buffer) {
        nsDetector det = new nsDetector(nsDetector.ALL);
        boolean isAscii = det.isAscii(buffer, buffer.length);
        if (isAscii) {
            return "US-ASCII";
        } else {
            det.DoIt(buffer, buffer.length, false);
        }
        det.DataEnd();
        return det.getProbableCharsets()[0];
    }

    public CSVData detectCSVParameters(Reader reader) throws IOException {
        List<Context> contexts = new ArrayList<Context>(settingsList.length);
        for (CSVSnifferSettings settings: settingsList) {
            contexts.add(new Context(settings));
        }
        int c = reader.read();
        int i = 1;
        while (c != -1) {
            for (Context context : contexts) {
                context.handle((char) c);
            }
            c = reader.read();
            i++;
        }
        for (Context context : contexts) {
            CSVData data = context.evaluate();
            if (data != null) {
                return data;
            }
        }
        return null;
    }

    byte[] readToBuffer(InputStream is) throws IOException {
        int bufferSize = this.bufferSize;
        return Util.readToBuffer(is, bufferSize);
    }

}
