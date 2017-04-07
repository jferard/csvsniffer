package com.github.jferard.csvsniffer.csd;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * A CSDValidationResult is composed of CSVValidationErrors.
 */
public class CSDValidationResult<F extends CSDField> implements Iterable<CSDValidationError>{
    private final List<CSDValidationError> errors;
    private Logger logger;
    private SizedIterable<F> schemaPattern;

    public CSDValidationResult(Logger logger, SizedIterable<F> schemaPattern) {
        this.logger = logger;
        this.schemaPattern = schemaPattern;
        this.errors = new ArrayList<CSDValidationError>(1000);
    }

    public void noLine() {
        String msg = "No available line.";
        this.errors.add(new CSDValidationError(0, CSDValidationError.Type.NO_AVAILABLE_LINE, msg));
        this.logger.severe("Line 0: "+ msg);
    }

    public void schemaHasTooManyFieldsForRecord(int line, CSVRecord record) {
        String msg = "The schema pattern (" + this.schemaPattern + ") has too many fields for the record " + record + ".";
        this.errors.add(new CSDValidationError(line, CSDValidationError.Type.TOO_MANY_LINES_FOR_RECORD, msg));
        this.logger.severe("Line "+line+": "+ msg);
    }

    public void incorrectValue(int line, String value, F field) {
        String msg = "The value '" + value + "' does not match with the field format '" + field + "'.";
        this.errors.add(new CSDValidationError(line, CSDValidationError.Type.INCORRECT_VALUE, msg));
        this.logger.severe("Line "+line+": "+msg);
    }

    public void schemaHasTooManyFieldsForHeader(CSVRecord firstRecord) {
        String msg = "The schema pattern (" + this.schemaPattern + ") has too many fields for the first record " + firstRecord + ".";
        this.errors.add(new CSDValidationError(1, CSDValidationError.Type.TOO_MANY_LINES_FOR_FIRST_RECORD, msg));
        this.logger.severe("Line 1: "+ msg);
    }

    public void incorrectColumnName(String columnName, String value) {
        String msg = "The column name read '" + value + "' does not match the schema : '"+value + "'.";
        this.errors.add(new CSDValidationError(1, CSDValidationError.Type.INCORRECT_COLUMN_NAME, msg));
        this.logger.severe("Line 1: "+ msg);
    }

    public boolean isOk() {
        return this.errors.isEmpty();
    }

    public void missingHeader() {
        String msg = "The first line values matches, but the header is missing.";
        this.errors.add(new CSDValidationError(1, CSDValidationError.Type.MISSING_HEADER, msg));
        this.logger.severe("Line 1: "+msg);
    }

    public void badHeader() {
        String msg = "The header is not good.";
        this.errors.add(new CSDValidationError(1, CSDValidationError.Type.BAD_HEADER, msg));
        this.logger.severe("Line 1: "+msg);
    }

    public int errorCount() {
        return this.errors.size();
    }

    @Override
    public Iterator<CSDValidationError> iterator() {
        return this.errors.iterator();
    }
}
