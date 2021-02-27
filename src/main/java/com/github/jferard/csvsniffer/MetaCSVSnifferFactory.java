package com.github.jferard.csvsniffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MetaCSVSnifferFactory {
    public static final List<String> DEFAULT_NULL_WORDS = Arrays.asList("<null>", "null", "");
    public static final List<String> DEFAULT_COLUMN_TYPES = Arrays.asList(
            "boolean/true/false", "boolean/yes/no", "boolean/T/F",
            "boolean/1/0", "integer", "datetime/yyyy-MM-dd'T'HH:mm:ss", "date/yyyy-MM-dd",
            "percentage/post/%/float//.",
            "currency/pre/$/float//.", "float//.");
    public static final List<String> FRANCE_NULL_WORDS = Arrays.asList("<nul>", "nul", "");
    public static final List<String> FRANCE_COLUMN_TYPES = Arrays.asList(
            "boolean/vrai/faux", "boolean/oui/non", "boolean/V/F", "boolean/1/0", "integer",
            "datetime/dd\\/MM\\/yyyy' 'HH:mm:ss", "date/dd\\/MM\\/yyyy", "date/dd\\/MM\\/yy",
            "percentage/post/%/float//,", "currency/post/€/float//,", "float//,");
    public static final MetaCSVSnifferFactory INSTANCE;

    static {
        INSTANCE = new MetaCSVSnifferFactory();
        INSTANCE.addLocale(Locale.US, DEFAULT_NULL_WORDS, DEFAULT_COLUMN_TYPES);
        INSTANCE.addLocale(Locale.FRANCE, FRANCE_NULL_WORDS, FRANCE_COLUMN_TYPES);
    }

    public static MetaCSVSniffer create() {
        return MetaCSVSnifferFactory.create(Locale.US);
    }

    public static MetaCSVSniffer create(Locale... locales) {
        return INSTANCE.createMetaCSVSniffer(locales);
    }

    private final Map<Locale, List<String>> nullWordsByLocale;
    private final Map<Locale, List<String>> columnTypesByLocale;

    public MetaCSVSnifferFactory() {
        this.nullWordsByLocale = new HashMap<Locale, List<String>>();
        this.columnTypesByLocale = new HashMap<Locale, List<String>>();
    }

    private MetaCSVSniffer createMetaCSVSniffer(Locale[] locales) {
        List<String> nullWords = new ArrayList<String>();
        List<String> columnTypes = new ArrayList<String>();
        boolean us = false;
        for (Locale locale : locales) {
            if (locale.equals(Locale.US)) {
                us = true;
            }
            List<String> localeNullWords = this.nullWordsByLocale.get(locale);
            if (localeNullWords != null) {
                nullWords.addAll(localeNullWords);
            }
            List<String> localeColumnTypes = this.columnTypesByLocale.get(locale);
            if (localeColumnTypes != null) {
                columnTypes.addAll(localeColumnTypes);
            }
        }
        if (!us) {
            nullWords.addAll(DEFAULT_NULL_WORDS);
            columnTypes.addAll(DEFAULT_COLUMN_TYPES);
        }
        return MetaCSVSniffer.create(Util.removeDuplicates(nullWords), Util.removeDuplicates(columnTypes));
    }

    public void addLocale(Locale locale, List<String> nullWords, List<String> columnTypes) {
        this.nullWordsByLocale.put(locale, nullWords);
        this.columnTypesByLocale.put(locale, columnTypes);
    }

}
