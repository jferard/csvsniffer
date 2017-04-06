package com.github.jferard.csvsniffer.csd;

import java.util.logging.Logger;

/**
 * Returns true iff the column matches exactly.
 */
class ExactColumnMatcher implements ColumnMatcher {
    private Logger logger;

    ExactColumnMatcher(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean match(String expected, String actual) {
        if (expected.equals(actual))
            return true;
        else {
            this.logger.fine("The column names are different. Expected : '"+expected+"'. Actual: '"+actual+"'.");
            return false;
        }
    }
}
