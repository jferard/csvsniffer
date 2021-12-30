package com.github.jferard.csvsniffer;

import java.util.Collection;
import java.util.List;

public class TestHelper {
    static <T> Counter<T> getCounter(final T element, final int n) {
        final Counter<T> c = new Counter<T>();
        c.add(element, n);
        return c;
    }

    static <T> Counter<T> getCounter(final Collection<T> elements) {
        final Counter<T> c = new Counter<T>();
        for (final T element : elements) {
            c.add(element);
        }
        return c;
    }

    static <T> MultiCounter<T> getMultiCounter(final List<Counter<T>> counters) {
        final MultiCounter<T> mc = new MultiCounter<T>();
        for (final Counter<T> counter : counters) {
            mc.update(counter);
        }
        return mc;
    }
}
