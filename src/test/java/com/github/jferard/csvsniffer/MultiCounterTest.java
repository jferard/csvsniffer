package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiCounterTest {
    @Test
    public void testGet() {
        MultiCounter<String> c = new MultiCounter<String>();
        c.add("foo", 3);
        c.add("bar", 2);
        c.add("baz", 1);
        c.add("foo", 1);
        Assert.assertEquals(Arrays.asList(3, 1), c.get("foo"));
        Assert.assertEquals(Collections.emptyList(), c.get("other"));
    }

    @Test
    public void testDescKeys() {
        MultiCounter<String> c = new MultiCounter<String>();
        c.add("baz", 1);
        c.add("foo", 3);
        c.add("bar", 2);
        c.add("foo", 1);
        Assert.assertEquals(Arrays.asList("foo", "bar", "baz"), c.descKeys());
    }

    @Test
    public void testEntrySet() {
        MultiCounter<String> c = new MultiCounter<String>();
        c.add("baz", 1);
        c.add("foo", 3);
        c.add("bar", 2);
        c.add("foo", 1);

        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("baz", 1);
        m.put("foo", 4);
        m.put("bar", 2);

//        Assert.assertEquals(m.entrySet(), c.entrySet());
    }

    @Test
    public void testUpdate() {
        Map<String, List<Integer>> m = new HashMap<String, List<Integer>>();
        m.put("baz", new ArrayList<Integer>(Arrays.asList(1)));
        m.put("foo", new ArrayList<Integer>(Arrays.asList(3, 1)));
        m.put("bar", new ArrayList<Integer>(Arrays.asList(2)));

        Map<String, Integer> m2 = new HashMap<String, Integer>();
        m2.put("bar", 1);

        Counter<String> c = new Counter<String>(m2);
        MultiCounter<String> c1 = new MultiCounter<String>(m);
        MultiCounter<String> c2 = new MultiCounter<String>(c1);
        c2.update(c);
        Assert.assertEquals(Arrays.asList(2, 1), c2.get("bar"));
    }

    @Test
    public void testToString() {
        MultiCounter<String> c = new MultiCounter<String>();
        c.add("foo", 3);
        c.add("bar", 2);
        Assert.assertEquals("MultiCounter{countsByElement={bar=[2], foo=[3]}}", c.toString());
    }

    @Test
    public void testFirstOr() {
//        MultiCounter<String> c = new MultiCounter<String>();
//        Assert.assertNull(c.firstOrNull());
//        Assert.assertEquals("foo", c.firstOr("foo"));
//        c.add("foo", 3);
//        c.add("bar", 2);
//        Assert.assertEquals("foo", c.firstOrNull());
    }
}