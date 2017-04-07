package com.github.jferard.csvsniffer.csd;

import org.apache.commons.csv.CSVRecord;

import java.util.logging.Logger;

/**
 */
class CSDValidatorHelper<F extends CSDField> {
    private Logger logger;
    private ColumnMatcher matcher;

    public CSDValidatorHelper(Logger logger, ColumnMatcher matcher) {
        this.logger = logger;
        this.matcher = matcher;
    }

    public int validateHeader(CSDValidationResult<F> result, SizedIterable<F> fields, CSVRecord firstRecord) {
        int headerErrorCount = 0;
        if (firstRecord.size() < fields.size()) {
            result.schemaHasTooManyFieldsForHeader(firstRecord);
            return -1;
        }

        int j = 0;
        for (F field : fields) {
            if (field.getCode().equals("*"))
                break;

            String columnName = field.getColumnName();
            String value = firstRecord.get(j++);
            if (!matcher.match(columnName, value))
                result.incorrectColumnName(columnName, value);
            headerErrorCount++;
        }
        return headerErrorCount;
    }

    public int validateRecord(CSDValidationResult<F> result, SizedIterable<F> fields, CSVRecord record, int line) {
        if (record.size() < fields.size()) {
            result.schemaHasTooManyFieldsForRecord(line, record);
            return -1;
        }

        int errorCount = 0;
        int j = 0;
        for (F field : fields) {
            if (field.getCode().equals("*"))
                break;

            String value = record.get(j++);
            if (!field.validate(value)) {
                result.incorrectValue(line, value, field);
                errorCount++;
            }
        }
        return errorCount;
    }

}
