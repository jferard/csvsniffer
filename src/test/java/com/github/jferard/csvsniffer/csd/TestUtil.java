package com.github.jferard.csvsniffer.csd;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by jferard on 07/04/17.
 */
public class TestUtil {
    public static CSDField getMandatoryField() {
        return new CSDField() {
            @Override
            public String getCode() {
                return "code";
            }

            @Override
            public String getType() {
                return "type";
            }

            @Override
            public String getColumnName() {
                return "name";
            }

            @Override
            public boolean isOptional() {
                return false;
            }

            @Override
            public boolean validate(String value) {
                return true;
            }
        };
    }

    public static CSDField getOptionalField() {
        return new CSDField() {
            @Override
            public String getCode() {
                return "code";
            }

            @Override
            public String getType() {
                return "type";
            }

            @Override
            public String getColumnName() {
                return "name";
            }

            @Override
            public boolean isOptional() {
                return true;
            }

            @Override
            public boolean validate(String value) {
                return true;
            }
        };
    }

    public static CSDField getStarField() {
        return new CSDField() {
            @Override
            public String getCode() {
                return "*";
            }

            @Override
            public String getType() {
                return "type";
            }

            @Override
            public String getColumnName() {
                return "name";
            }

            @Override
            public boolean isOptional() {
                return true;
            }

            @Override
            public boolean validate(String value) {
                return true;
            }
        };
    }

    public static <F> SizedIterable<F> fromCollection(final Collection<F> c) {
        return new SizedIterable<F>() {
            @Override
            public int size() {
                return c.size();
            }

            @Override
            public Iterator<F> iterator() {
                return c.iterator();
            }
        };
    }
}
