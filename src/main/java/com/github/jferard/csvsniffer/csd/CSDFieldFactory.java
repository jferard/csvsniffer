package com.github.jferard.csvsniffer.csd;

/**
 * Created by jferard on 02/04/17.
 */
public interface CSDFieldFactory<T extends CSDField> {
    T create(String type, String code, String columnName, boolean optional);
}
