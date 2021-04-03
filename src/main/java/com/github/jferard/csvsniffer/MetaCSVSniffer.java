package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.CSVFormatHelper;
import com.github.jferard.javamcsv.ColTypeParser;
import com.github.jferard.javamcsv.MetaCSVData;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVParserBuilder;
import com.github.jferard.javamcsv.MetaCSVReadException;
import com.github.jferard.javamcsv.description.FieldDescription;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MetaCSVSniffer {
    public static MetaCSVSniffer create() {
        return MetaCSVSnifferFactory.create(Locale.US);
    }

    public static MetaCSVSniffer create(Locale locale) {
        return MetaCSVSnifferFactory.create(locale);
    }

    public static MetaCSVSniffer create(List<String> nullWords, List<String> columnTypes) {
        CSVSniffer csvSniffer = CSVSniffer.create();
        final List<FieldDescription<?>> descriptions =
                new ArrayList<FieldDescription<?>>(columnTypes.size());
        for (String columnType : columnTypes) {
            try {
                FieldDescription<?> description =
                        new ColTypeParser(MetaCSVParserBuilder.DEFAULT_OBJECT_PARSER)
                                .parseColType(columnType);
                descriptions.add(description);
            } catch (MetaCSVParseException e) {
                // pass
            }
        }
        return new MetaCSVSniffer(csvSniffer, nullWords, descriptions);
    }

    private final CSVSniffer csvSniffer;
    private final List<FieldDescription<?>> descriptions;
    private final List<String> nullWords;

    MetaCSVSniffer(CSVSniffer csvSniffer, List<String> nullWords,
                   List<FieldDescription<?>> descriptions) {
        this.csvSniffer = csvSniffer;
        this.nullWords = nullWords;
        this.descriptions = descriptions;
    }


    public MetaCSVData sniff(InputStream is)
            throws IOException {
        byte[] buffer = Util.readToBuffer(is, this.csvSniffer.bufferSize);
        String charset = this.csvSniffer.detectCharset(buffer);
        BufferedReader reader = this.getReader(buffer, charset);
        try {
            CSVData data = this.csvSniffer.detectCSVParameters(reader);

            CSVFormat csvFormat = CSVFormatHelper.getCSVFormat(data);
            CSVParser parser = new CSVParser(this.getReader(buffer, charset), csvFormat);
            List<List<String>> cols = this.getCols(parser);
            String nullValue = this.getNullValue(cols);
            Map<Integer, FieldDescription<?>> fieldDescriptionByCol =
                    new HashMap<Integer, FieldDescription<?>>();
            for (int c = 0; c < cols.size(); c++) {
                FieldDescription<?> fieldDescription =
                        this.getDescription(Util.removeDuplicates(cols.get(c)), nullValue);
                if (fieldDescription != null) {
                    fieldDescriptionByCol.put(c, fieldDescription);
                }
            }
            return data.toMetaCSVData(charset, nullValue, fieldDescriptionByCol);
        } finally {
            reader.close(); // should not be necessary (byte array)
        }
    }

    private String getNullValue(List<List<String>> cols) {
        if (this.nullWords.isEmpty()) {
            return "";
        }
        final Map<String, Integer> countByNullWord = new HashMap<String, Integer>();
        for (String nullWord : this.nullWords) {
            countByNullWord.put(nullWord, 0);
        }
        countByNullWord.put("", 1);
        for (List<String> col : cols) {
            for (String value : col) {
                Integer count = countByNullWord.get(value);
                if (count != null) {
                    countByNullWord.put(value, count + 1);
                }
            }
        }
        return Collections.max(this.nullWords, new Comparator<String>() {
            @Override
            public int compare(String nw1, String nw2) {
                return countByNullWord.get(nw1) - countByNullWord.get(nw2);
            }
        });
    }

    private BufferedReader getReader(byte[] buffer, String charset)
            throws UnsupportedEncodingException {
        return new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buffer), charset));
    }

    private FieldDescription<?> getDescription(List<String> values, String nullValue) {
        if (this.areAllVoid(values, nullValue)) {
            return null;
        }
        for (FieldDescription<?> description : this.descriptions) {
            try {
                for (String value : values) {
                    description.toFieldProcessor(nullValue).toObject(value);
                }
                return description;
            } catch (MetaCSVReadException e) {
                // pass
            }
        }
        return null;
    }

    private boolean areAllVoid(List<String> values, String nullValue) {
        for (String value : values) {
            if (!(value.isEmpty() || value.equals(nullValue))) {
                return false;
            }
        }
        return true;
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