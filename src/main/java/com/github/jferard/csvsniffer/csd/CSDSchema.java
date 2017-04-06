package com.github.jferard.csvsniffer.csd;

import java.util.Collection;
import java.util.Iterator;

/**
 * A CSDSchema is a collection of CSDFields, without "wildcard" field.
 */
public class CSDSchema<F extends CSDField> implements SizedIterable<F> {
    private Collection<F> fields;
    private boolean optionalHeader;

    public CSDSchema(Collection<F> fields, boolean optionalHeader) {
        this.fields = fields;
        this.optionalHeader = optionalHeader;
    }

    public int size() {
        return this.fields.size();
    }

    @Override
    public Iterator<F> iterator() {
        return this.fields.iterator();
    }

    @Override
    public String toString() {
        return this.fields.toString();
    }

    public String getColumns() {
        StringBuilder sb = new StringBuilder("(");
        Iterator<F> it = fields.iterator();
        if (it.hasNext()) {
            sb.append(it.next().getCode());
            while (it.hasNext())
                sb.append(", ").append(it.next().getCode());
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean hasOptionalHeader() {
        return this.optionalHeader;
    }
}
