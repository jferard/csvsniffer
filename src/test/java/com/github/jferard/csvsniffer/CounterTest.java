package com.github.jferard.csvsniffer;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CounterTest {
    @Test
    public void testGet() {
        Counter<String> c = new Counter<String>();
        Assert.assertTrue(c.isEmpty());
        c.add("foo", 3);
        Assert.assertFalse(c.isEmpty());
        c.add("bar", 2);
        c.add("baz");
        c.add("foo");
        Assert.assertEquals(4, c.get("foo"));
    }

    @Test
    public void testDescKeys() {
        Counter<String> c = new Counter<String>();
        c.add("baz");
        c.add("foo", 3);
        c.add("bar", 2);
        c.add("foo");
        Assert.assertEquals(Arrays.asList("foo", "bar", "baz"), c.descKeys());
    }

    @Test
    public void testEntrySet() {
        Counter<String> c = new Counter<String>();
        c.add("baz");
        c.add("foo", 3);
        c.add("bar", 2);
        c.add("foo");

        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("baz", 1);
        m.put("foo", 4);
        m.put("bar", 2);

        Assert.assertEquals(m.entrySet(), c.entrySet());
    }

    @Test
    public void testUpdate() {
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("baz", 1);
        m.put("foo", 4);
        m.put("bar", 2);

        Counter<String> c = new Counter<String>(m);
        Counter<String> c2 = new Counter<String>(c);
        c2.update(c);
        Assert.assertEquals(4, c2.get("bar"));
    }

    @Test
    public void testToString() {
        Counter<String> c = new Counter<String>();
        c.add("foo", 3);
        c.add("bar", 2);
        Assert.assertEquals("Counter{countByElement={bar=2, foo=3}}", c.toString());
    }

    @Test
    public void testFirstOr() {
        Counter<String> c = new Counter<String>();
        Assert.assertNull(c.firstOrNull());
        Assert.assertEquals("foo", c.firstOr("foo"));
        c.add("foo", 3);
        c.add("bar", 2);
        Assert.assertEquals("foo", c.firstOrNull());
    }

    @Test
    public void testEquals() {
        Counter<String> c = new Counter<String>();
        c.add("foo", 3);
        c.add("bar", 2);
        Counter<String> c2 = new Counter<String>();
        c2.add("foo", 3);
        c2.add("bar", 2);
        Counter<String> c3 = new Counter<String>();
        c3.add("foo", 2);
        c3.add("bar", 2);

        Assert.assertEquals(c, c);
        Assert.assertEquals(c, c);
        Assert.assertNotEquals(c3, c);
        Assert.assertNotEquals(new Object(), c);
    }

    @Test
    public void testHashcode() {
        Counter<String> c = new Counter<String>();
        c.add("foo", 3);
        c.add("bar", 2);

        Assert.assertEquals(198870, c.hashCode());
    }
}