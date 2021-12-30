package com.github.jferard.csvsniffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiCounter<T> {
    private final Map<T, List<Integer>> countsByElement;

    public MultiCounter() {
        this(new HashMap<T, List<Integer>>());
    }

    public MultiCounter(final Map<T, List<Integer>> countByElement) {
        this.countsByElement = countByElement;
    }

    public MultiCounter(final MultiCounter<T> other) {
        this(other.countsByElement);
    }

    public void add(final T element, final int count) {
        List<Integer> curCounts = this.countsByElement.get(element);
        if (curCounts == null) {
            curCounts = new ArrayList<Integer>();
            this.countsByElement.put(element, curCounts);
        }
        curCounts.add(count);
    }

    public List<Integer> get(final T element) {
        final List<Integer> curCounts = this.countsByElement.get(element);
        if (curCounts == null) {
            return new ArrayList<Integer>();
        } else {
            return curCounts;
        }
    }

    public void update(final Counter<T> counter) {
        for (final Map.Entry<T, Integer> entry : counter.entrySet()) {
            final T element = entry.getKey();
            final Integer count = entry.getValue();
            this.add(element, count);
        }
    }

    public List<T> descKeys() {
        final List<T> elements = new ArrayList<T>(this.countsByElement.keySet());
        Collections.sort(elements, new Comparator<T>() {
            @Override
            public int compare(final T c1, final T c2) {
                return MultiCounter.this.countsByElement.get(c2).size() -
                        MultiCounter.this.countsByElement.get(c1).size();
            }
        });
        return elements;
    }

    @Override
    public String toString() {
        return "MultiCounter{" +
                "countsByElement=" + this.countsByElement +
                '}';
    }
}
