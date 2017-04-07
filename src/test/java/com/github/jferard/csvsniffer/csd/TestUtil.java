package com.github.jferard.csvsniffer.csd;

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
}
