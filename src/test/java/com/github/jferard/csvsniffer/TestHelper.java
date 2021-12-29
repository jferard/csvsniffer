package com.github.jferard.csvsniffer;

import java.util.Collection;
import java.util.List;

public class TestHelper {
    static <T> Counter<T> getCounter(T element, int n) {
        Counter<T> c = new Counter<T>();
        c.add(element, n);
        return c;
    }

    static <T> Counter<T> getCounter(Collection<T> elements) {
        Counter<T> c = new Counter<T>();
        for (T element : elements) {
            c.add(element);
        }
        return c;
    }

    static <T> MultiCounter<T> getMultiCounter(List<Counter<T>> counters) {
        MultiCounter<T> mc = new MultiCounter<T>();
        for (Counter<T> counter : counters) {
            mc.update(counter);
        }
        return mc;
    }
}
