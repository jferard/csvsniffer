package com.github.jferard.csvsniffer.csd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by jferard on 06/04/17.
 */
public class ExactColumnMatcherTest {
    private ColumnMatcher matcher;

    @Before
    public void setUp() {
        Logger logger = PowerMock.createMock(Logger.class);
        matcher = new ExactColumnMatcher(logger);
    }

    @Test
    public void match() throws Exception {
        Assert.assertTrue(matcher.match("abcde", "abcde"));
        Assert.assertFalse(matcher.match("abcde", "abcdef"));
    }

}