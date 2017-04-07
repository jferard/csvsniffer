package com.github.jferard.csvsniffer.csd;

/**
 * A CSDValidationError is an element of a CSDValidationResult
 */
public class CSDValidationError {
    private final int line;
    private final Type type;
    private final String msg;

    public enum Type {
        INCORRECT_VALUE, TOO_MANY_LINES_FOR_RECORD, TOO_MANY_LINES_FOR_FIRST_RECORD, INCORRECT_COLUMN_NAME, MISSING_HEADER, BAD_HEADER, NO_AVAILABLE_LINE,
    }

    CSDValidationError(int line, Type type, String msg) {
        this.line = line;
        this.type = type;
        this.msg = msg;
    }

    public String toString() {
        return "CSDValidationError of type "+type+": "+msg+" (line "+line+")";
    }
}
