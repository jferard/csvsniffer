package com.github.jferard.csvsniffer;

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

    public CSVSniffer(final int bufferSize, final CSVSnifferSettings... settingsList) {
        this.bufferSize = bufferSize;
        this.settingsList = settingsList;
        this.charset = null;
    }

    /**
     * Sniff the input stream to detect the charset and the csv parameters
     * @param is the input stream
     * @return the extended csv data
     * @throws IOException if an I/O error occurs
     */
    public ExtendedCSVData sniff(final InputStream is) throws IOException {
        final byte[] buffer = Util.readToBuffer(is, this.bufferSize);
        this.charset = this.detectCharset(buffer);
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buffer), this.charset));
        try {
            final CSVData data = this.detectCSVParameters(reader);
            return new ExtendedCSVData(this.charset, data);
        } finally {
            reader.close(); // should not be necessary (byte array)
        }
    }

    /**
     * Detect the charset given a byte array.
     * @param buffer the byte array
     * @return the name of the charset
     */
    public String detectCharset(final byte[] buffer) {
        final nsDetector det = new nsDetector(nsDetector.ALL);
        final boolean isAscii = det.isAscii(buffer, buffer.length);
        if (isAscii) {
            return "US-ASCII";
        } else {
            det.DoIt(buffer, buffer.length, false);
        }
        det.DataEnd();
        return det.getProbableCharsets()[0];
    }

    /**
     * Detect the csv parameters
     * @param reader the input
     * @return the csv data
     * @throws IOException if an I/O error occurs
     */
    public CSVData detectCSVParameters(final Reader reader) throws IOException {
        final List<Context> contexts = new ArrayList<Context>(this.settingsList.length);
        for (final CSVSnifferSettings settings: this.settingsList) {
            contexts.add(new Context(settings));
        }
        int c = reader.read();
        int i = 1;
        while (c != -1) {
            for (final Context context : contexts) {
                context.handle((char) c);
            }
            c = reader.read();
            i++;
        }
        for (final Context context : contexts) {
            final CSVData data = context.evaluate();
            if (data != null) {
                return data;
            }
        }
        return null;
    }
}
