package com.github.jferard.csvsniffer;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testRemoveDuplicates() {
        assertEquals(Arrays.asList(0, 1, 2, 3), Util.removeDuplicates(Arrays.asList(0, 1, 2, 3, 0, 0)));
    }

    @Test
    public void testBadRemoveDuplicates() {
        assertEquals(Arrays.asList(0, 10, 2, 3), Util.removeDuplicates(Arrays.asList(0, 10, 10, 0, 10, 2, 10, 2, 2, 3, 0, 3, 0)));
    }
}