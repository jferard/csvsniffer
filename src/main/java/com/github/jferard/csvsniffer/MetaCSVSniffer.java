package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.CSVFormatHelper;
import com.github.jferard.javamcsv.ColTypeParser;
import com.github.jferard.javamcsv.FieldDescription;
import com.github.jferard.javamcsv.MetaCSVData;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVReadException;
import com.github.jferard.javamcsv.MetaCSVReader;
import com.github.jferard.javamcsv.MetaCSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MetaCSVSniffer {
    public static MetaCSVSniffer create() {
        CSVSniffer csvSniffer = CSVSniffer.create();
        return new MetaCSVSniffer(csvSniffer, "boolean/X", "boolean/T/F", "boolean/1/0",
                "boolean/Y/N",
                "integer",
                "datetime/yyyy-MM-dd'T'HH:mm:ss", "date/yyyy-MM-dd", "percentage/...",
                "currency/...", "float//.");
    }

    private final CSVSniffer csvSniffer;
    private final List<FieldDescription<?>> descriptions;

    public MetaCSVSniffer(CSVSniffer csvSniffer, String... colTypes) {
        this.csvSniffer = csvSniffer;
        this.descriptions = new ArrayList<FieldDescription<?>>(colTypes.length);
        for (String colType : colTypes) {
            try {
                FieldDescription<?> description = new ColTypeParser().parseColType(colType);
                this.descriptions.add(description);
            } catch (MetaCSVParseException e) {
                // pass
            }
        }
    }


    public MetaCSVData sniff(InputStream is)
            throws IOException, MetaCSVParseException {
        byte[] buffer = Util.readToBuffer(is, csvSniffer.bufferSize);
        String charset = csvSniffer.detectCharset(buffer);
        BufferedReader reader = getReader(buffer, charset);
        try {
            CSVData data = csvSniffer.detectCSVParameters(reader);

            CSVFormat csvFormat = CSVFormatHelper.getCSVFormat(data);
            CSVParser parser = new CSVParser(getReader(buffer, charset), csvFormat);
            List<List<String>> cols = getCols(parser);
            Map<Integer, FieldDescription<?>> fieldDescriptionByCol =
                    new HashMap<Integer, FieldDescription<?>>();
            for (int c = 0; c < cols.size(); c++) {
                FieldDescription<?> fieldDescription = getDescription(cols.get(c));
                if (fieldDescription != null) {
                    fieldDescriptionByCol.put(c, fieldDescription);
                }
            }
            return data.toMetaCSVData(charset, "", fieldDescriptionByCol);
        } finally {
            reader.close(); // should not be necessary (byte array)
        }
    }

    private BufferedReader getReader(byte[] buffer, String charset)
            throws UnsupportedEncodingException {
        return new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buffer), charset));
    }

    private FieldDescription<?> getDescription(List<String> values) throws MetaCSVParseException {
        for (FieldDescription<?> description : this.descriptions) {
            try {
                for (String value : values) {
                    description.toFieldProcessor("").toObject(value);
                }
                return description;
            } catch (MetaCSVReadException e) {
                // pass
            }
        }
        return null;
    }

    private List<List<String>> getCols(CSVParser parser) {
        List<List<String>> cols = new ArrayList<List<String>>();
        Iterator<CSVRecord> iterator = parser.iterator();
        try {
            if (!iterator.hasNext()) {
                return cols;
            }
            CSVRecord header = iterator.next();
            int size = header.size();
            for (int c = 0; c < size; c++) {
                cols.add(c, new ArrayList<String>());
            }
            while (iterator.hasNext()) {
                CSVRecord record = iterator.next();
                for (int c = 0; c < record.size(); c++) {
                    cols.get(c).add(record.get(c));
                }
                for (int c = record.size(); c < size; c++) {
                    cols.get(c).add("");
                }
            }
        } catch (RuntimeException e) {
            // EOF reached before encapsulated token finished
        }
        return cols;
    }
}