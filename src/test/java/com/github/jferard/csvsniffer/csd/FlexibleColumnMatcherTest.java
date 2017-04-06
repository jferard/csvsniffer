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
public class FlexibleColumnMatcherTest {
    private ColumnMatcher matcher;
    private CSDUtil util;

    @Before
    public void setUp() {
        Logger logger = PowerMock.createNiceMock(Logger.class);
        util = new CSDUtil(logger);
        matcher = new FlexibleColumnMatcher(logger, util);
    }

    @Test
    public void match() throws Exception {
        PowerMock.replayAll();
        Assert.assertTrue(matcher.match("abcde", "abcde"));
        Assert.assertTrue(matcher.match("abcde", "abcdef"));
        Assert.assertFalse(matcher.match("abcde", "abcdefghij"));
        PowerMock.verifyAll();
    }

}