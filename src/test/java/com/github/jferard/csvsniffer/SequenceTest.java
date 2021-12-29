package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class SequenceTest {
    @Test
    public void testClean() {
        Sequence s = new Sequence(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 100, 0)));
        Assert.assertEquals(155, s.sum());
        s.clean(10);
        Assert.assertEquals(55, s.sum());
        s.clean(10);
        Assert.assertEquals(45, s.sum());
        s.clean(10);
        Assert.assertEquals(45, s.sum());
    }

    @Test
    public void testMean() {
        Sequence s = new Sequence(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        Assert.assertEquals(5.0, s.mean(11), 0.01);
    }

    @Test
    public void testVariance() {
        Sequence s = new Sequence(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        Assert.assertEquals(8.25, s.variance(10), 0.01);
    }

    @Test
    public void testScore() {
        Sequence s = new Sequence(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        Assert.assertEquals(0.825, s.score(10), 0.01);
    }
}