package com.github.jferard.csvsniffer.csd;

import java.util.logging.Logger;

/**
 * Returns true iff the Levensthein distance is less or equal than 2.
 */
class FlexibleColumnMatcher implements ColumnMatcher {
    private Logger logger;
    private CSDUtil util;

    public FlexibleColumnMatcher(Logger logger, CSDUtil util) {
        this.logger = logger;
        this.util = util;
    }
    @Override
    public boolean match(String expected, String actual) {
        if (expected.equals(actual))
            return true;
        else if (this.util.levenshteinDistance(this.util.stripAccents(expected), this.util.stripAccents(actual)) <= 2) {
            this.logger.fine("The column names are close but not equal. Expected : '"+expected+"'. Actual: '"+actual+"'.");
            return true;
        } else
            this.logger.fine("The column names are different. Expected : '"+expected+"'. Actual: '"+actual+"'.");
            return false;
    }
}
