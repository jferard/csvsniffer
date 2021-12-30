package com.github.jferard.csvsniffer;

import com.github.jferard.javamcsv.CSVFormatHelper;
import com.github.jferard.javamcsv.ColTypeParser;
import com.github.jferard.javamcsv.MetaCSVData;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVParserBuilder;
import com.github.jferard.javamcsv.MetaCSVReadException;
import com.github.jferard.javamcsv.MetaCSVRenderer;
import com.github.jferard.javamcsv.description.FieldDescription;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A meta csv sniffer. Returns a meta csv data
 */
public class MetaCSVSniffer {
    public static void main(final String[] args) throws IOException {
        if (args.length == 1) {
            final MetaCSVSniffer sniffer = MetaCSVSniffer.create();
            final MetaCSVData metaCSVData = sniffer.sniff(new FileInputStream(args[0]));
            final MetaCSVRenderer renderer = MetaCSVRenderer.create(System.out, false);
            renderer.render(metaCSVData);
        }
    }

    public static MetaCSVSniffer create() {
        return MetaCSVSnifferFactory.create();
    }

    public static MetaCSVSniffer create(final Locale locale) {
        return MetaCSVSnifferFactory.create(locale);
    }

    public static MetaCSVSniffer create(final List<String> nullWords, final List<String> columnTypes) {
        final CSVSniffer csvSniffer = CSVSniffer.create();
        final List<FieldDescription<?>> descriptions =
                new ArrayList<FieldDescription<?>>(columnTypes.size());
        for (final String columnType : columnTypes) {
            try {
                final FieldDescription<?> description =
                        new ColTypeParser(MetaCSVParserBuilder.DEFAULT_OBJECT_PARSER)
                                .parseColType(columnType);
                descriptions.add(description);
            } catch (final MetaCSVParseException e) {
                // pass
            }
        }
        return new MetaCSVSniffer(csvSniffer, nullWords, descriptions);
    }

    private static MetaCSVData fromExtendedData(final ExtendedCSVData extendedCSVData, final String nullValue,
                                                final Map<Integer, FieldDescription<?>> descriptionByColIndex) {
        final Map<String, String> meta = new HashMap<String, String>();
        meta.put("generator", "csvsniffer");
        return new MetaCSVData("draft0", meta, Charset.forName(extendedCSVData.getCharset()), false,
                extendedCSVData.getLineTerminator(),
                extendedCSVData.getDelimiter(), extendedCSVData.getQuoteChar(), extendedCSVData.isDoubleQuote(),
                extendedCSVData.getEscapeChar(),
                extendedCSVData.isSkipInitialSpace(),
                nullValue, descriptionByColIndex);
    }

    private final CSVSniffer csvSniffer;
    private final List<FieldDescription<?>> descriptions;
    private final List<String> nullWords;

    MetaCSVSniffer(final CSVSniffer csvSniffer, final List<String> nullWords,
                   final List<FieldDescription<?>> descriptions) {
        this.csvSniffer = csvSniffer;
        this.nullWords = nullWords;
        this.descriptions = descriptions;
    }


    public MetaCSVData sniff(final InputStream is)
            throws IOException {
        final byte[] buffer = Util.readToBuffer(is, this.csvSniffer.bufferSize);
        final String charset = this.csvSniffer.detectCharset(buffer);
        final BufferedReader reader = this.getReader(buffer, charset);
        try {
            final CSVData data = this.csvSniffer.detectCSVParameters(reader);

            final CSVFormat csvFormat = CSVFormatHelper.getCSVFormat(data);
            final CSVParser parser = new CSVParser(this.getReader(buffer, charset), csvFormat);
            final List<List<String>> cols = this.getCols(parser);
            final String nullValue = this.getNullValue(cols);
            final Map<Integer, FieldDescription<?>> fieldDescriptionByCol =
                    new HashMap<Integer, FieldDescription<?>>();
            for (int c = 0; c < cols.size(); c++) {
                final FieldDescription<?> fieldDescription =
                        this.getDescription(Util.removeDuplicates(cols.get(c)), nullValue);
                if (fieldDescription != null) {
                    fieldDescriptionByCol.put(c, fieldDescription);
                }
            }
            final ExtendedCSVData extended = new ExtendedCSVData(charset, data);
            return MetaCSVSniffer.fromExtendedData(extended, nullValue, fieldDescriptionByCol);
        } finally {
            reader.close(); // should not be necessary (byte array)
        }
    }

    private String getNullValue(final List<List<String>> cols) {
        if (this.nullWords.isEmpty()) {
            return "";
        }
        final Map<String, Integer> countByNullWord = new HashMap<String, Integer>();
        for (final String nullWord : this.nullWords) {
            countByNullWord.put(nullWord, 0);
        }
        countByNullWord.put("", 1);
        for (final List<String> col : cols) {
            for (final String value : col) {
                final Integer count = countByNullWord.get(value);
                if (count != null) {
                    countByNullWord.put(value, count + 1);
                }
            }
        }
        return Collections.max(this.nullWords, new Comparator<String>() {
            @Override
            public int compare(final String nw1, final String nw2) {
                return countByNullWord.get(nw1) - countByNullWord.get(nw2);
            }
        });
    }

    private BufferedReader getReader(final byte[] buffer, final String charset)
            throws UnsupportedEncodingException {
        return new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(buffer), charset));
    }

    private FieldDescription<?> getDescription(final List<String> values, final String nullValue) {
        if (this.areAllVoid(values, nullValue)) {
            return null;
        }
        for (final FieldDescription<?> description : this.descriptions) {
            try {
                for (final String value : values) {
                    description.toFieldProcessor(nullValue).toObject(value);
                }
                return description;
            } catch (final MetaCSVReadException e) {
                // pass
            }
        }
        return null;
    }

    private boolean areAllVoid(final List<String> values, final String nullValue) {
        for (final String value : values) {
            if (!(value.isEmpty() || value.equals(nullValue))) {
                return false;
            }
        }
        return true;
    }

    private List<List<String>> getCols(final CSVParser parser) {
        final List<List<String>> cols = new ArrayList<List<String>>();
        final Iterator<CSVRecord> iterator = parser.iterator();
        try {
            if (!iterator.hasNext()) {
                return cols;
            }
            final CSVRecord header = iterator.next();
            final int size = header.size();
            for (int c = 0; c < size; c++) {
                cols.add(c, new ArrayList<String>());
            }
            while (iterator.hasNext()) {
                final CSVRecord record = iterator.next();
                for (int c = 0; c < record.size(); c++) {
                    cols.get(c).add(record.get(c));
                }
                for (int c = record.size(); c < size; c++) {
                    cols.get(c).add("");
                }
            }
        } catch (final RuntimeException e) {
            // EOF reached before encapsulated token finished
        }
        return cols;
    }
}