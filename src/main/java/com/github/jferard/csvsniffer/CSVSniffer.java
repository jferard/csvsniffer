package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.MetaCSVData;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVSniffer {
    private final CSVSnifferSettings[] settingsList;
    String charset = null;
    private final int bufferSize;

    public CSVSniffer(int bufferSize, CSVSnifferSettings... settingsList) {
        this.bufferSize = bufferSize;
        this.settingsList = settingsList;
    }

    public MetaCSVData sniff(InputStream is) throws IOException {
        byte[] buffer = readToBuffer(is);
        charset = detectCharset(buffer);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buffer), charset));
        try {
            CSVData data = detectCSV(reader);
            return data.toMetaCSVData(charset);
        } finally {
            reader.close(); // should not be necessary (byte array)
        }
    }

    private String detectCharset(byte[] buffer) {
        nsDetector det = new nsDetector(nsDetector.ALL);
        final String[] cs = new String[1];
        boolean isAscii = det.isAscii(buffer, buffer.length);
        if (isAscii) {
            return "US-ASCII";
        } else {
            det.DoIt(buffer, buffer.length, false);
        }
        det.DataEnd();
        return det.getProbableCharsets()[0];
    }

    public CSVData detectCSV(Reader reader) throws IOException {
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

    private byte[] readToBuffer(InputStream is) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int count = is.read(buffer, 0, buffer.length);
        int total = count;
        while (count != -1 && total < buffer.length) {
            count = is.read(buffer, total, buffer.length - total);
            total += count;
        }
        if (total == bufferSize) {
            return buffer;
        }
        byte[] trimmedBuffer = new byte[total];
        System.arraycopy(buffer, 0, trimmedBuffer, 0, total);
        return trimmedBuffer;
    }
}
