package com.github.jferard.csvsniffer.csd;

/**
 * Created by jferard on 03/04/17.
 */
public interface ColumnMatcher {
    boolean match(String expected, String actual);
}
