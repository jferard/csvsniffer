package com.github.jferard.csvsniffer.csd;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by jferard on 06/04/17.
 */
public class CSDSchemaPatternTest {
    @Test
    public void testEmpty() {
        CSDSchemaPattern<CSDField> s = new CSDSchemaPattern<CSDField>(Collections.<CSDField>emptyList(), false);
        Assert.assertFalse(s.hasOptionalHeader());
        Assert.assertFalse(s.iterator().hasNext());
        Assert.assertEquals(0, s.size());
        Assert.assertEquals("[]", s.toString());
    }

    @Test
    public void testOneField() {
        CSDField f = new CSDField() {
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
        CSDSchemaPattern<CSDField> s = new CSDSchemaPattern<CSDField>(Arrays.asList(f), true);
        Assert.assertTrue(s.hasOptionalHeader());
        Assert.assertTrue(s.iterator().hasNext());
        Assert.assertEquals(f, s.iterator().next());
        Assert.assertEquals(1, s.size());
    }

    @Test
    public void testOneStartField() {
        CSDField f = new CSDField() {
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
                return false;
            }

            @Override
            public boolean validate(String value) {
                return true;
            }
        };
        CSDSchemaPattern<CSDField> s = new CSDSchemaPattern<CSDField>(Arrays.asList(f), true);
        Assert.assertTrue(s.hasOptionalHeader());
        Assert.assertTrue(s.iterator().hasNext());
        Assert.assertEquals(f, s.iterator().next());
        Assert.assertEquals(0, s.size());
    }
}