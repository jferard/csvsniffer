package com.github.jferard.csvsniffer.csd;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A CSDSchemaPattern is a collection of CSDFields, with or without "wildcard" field.
 */
public class CSDSchemaPattern<F extends CSDField> implements SizedIterable<F> {
    private Collection<F> fields;
    private boolean optionalHeader;

    public CSDSchemaPattern(Collection<F> fields, boolean optionalHeader) {
        this.fields = fields;
        this.optionalHeader = optionalHeader;
    }

    /**
     * @return the number of fields before a "wildcard" field, or the total number of fields.
     */
    public int size() {
        int schemaSize = 0;
        for (F field : fields) {
            if (field.getCode().equals("*"))
                break;

            schemaSize++;
        }
        return schemaSize;
    }

    @Override
    public Iterator<F> iterator() {
        return this.fields.iterator();
    }

    @Override
    public String toString() {
        return this.fields.toString();
    }

    public boolean hasOptionalHeader() {
        return this.optionalHeader;
    }

    public CSDSchema<F> newSchema(CSDFieldFactory<F> factory, CSVRecord firstRecord) {
        List<F> newFields = new ArrayList<F>(this.fields.size());

        for (F field : this.fields) {
            if (field.getCode().equals("*")) {
                this.addFields(factory, newFields, field, firstRecord, this.fields.size());
                break;
            }
            newFields.add(field);
        }
        System.out.println(newFields);
        return new CSDSchema<F>(newFields, this.optionalHeader);
    }

    private void addFields(CSDFieldFactory<F> factory, List<F> newFields, F field, CSVRecord firstRecord, int begin) {
        if (firstRecord == null)
            return;

        for (int i=begin; i<firstRecord.size(); i++) {
            String name = firstRecord.get(i);
            newFields.add(factory.create(field.getType(), name, name, true));
        }
    }
}
