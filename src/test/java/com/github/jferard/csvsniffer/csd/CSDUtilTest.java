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
public class CSDUtilTest {
    private CSDUtil util;

    @Before
    public void setUp() {
        Logger logger = PowerMock.createMock(Logger.class);
        util = new CSDUtil(logger);
    }

    @Test
    public void levenshteinDistance() {
        Assert.assertEquals(5, util.levenshteinDistance("abcde", "abcdefghij"));
        Assert.assertEquals(5, util.levenshteinDistance(this.util.stripAccents("abcde"), this.util.stripAccents("abcdefghij")));
    }

    @Test
    public void stripAccents() {
        Assert.assertEquals("abcde", util.stripAccents("Abcdé"));
    }
}