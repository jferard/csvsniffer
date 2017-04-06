package com.github.jferard.csvsniffer.csd;

/**
 * Created by jferard on 02/04/17.
 */
public interface CSDField {
    String getCode();

    String getType();

    String getColumnName();

    boolean isOptional();

    boolean validate(String value);
}
