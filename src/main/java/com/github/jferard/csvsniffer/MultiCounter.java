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

    public MultiCounter(Map<T, List<Integer>> countByElement) {
        this.countsByElement = countByElement;
    }

    public MultiCounter(MultiCounter other) {
        this(other.countsByElement);
    }

    public void add(T element, int count) {
        List<Integer> curCounts = this.countsByElement.get(element);
        if (curCounts == null) {
            curCounts = new ArrayList<Integer>();
            this.countsByElement.put(element, curCounts);
        }
        curCounts.add(count);
    }

    public void add(Counter<T> counter) {
        for (Map.Entry<T, Integer> entry : counter.entrySet()) {
            this.add(entry.getKey(), entry.getValue());
        }
    }

    public List<Integer> get(T element) {
        List<Integer> curCounts = this.countsByElement.get(element);
        if (curCounts == null) {
            return new ArrayList<Integer>();
        } else {
            return curCounts;
        }
    }

    public void update(Counter<T> counter) {
        for (Map.Entry<T, Integer> entry : counter.entrySet()) {
            T element = entry.getKey();
            Integer count = entry.getValue();
            this.add(element, count);
        }
    }

    public List<T> descKeys() {
        List<T> elements = new ArrayList<T>(this.countsByElement.keySet());
        Collections.sort(elements, new Comparator<T>() {
            @Override
            public int compare(T c1, T c2) {
                return MultiCounter.this.countsByElement.get(c2).size() -
                        MultiCounter.this.countsByElement.get(c1).size();
            }
        });
        return elements;
    }

    @Override
    public String toString() {
        return "MultiCounter{" +
                "countsByElement=" + countsByElement +
                '}';
    }
}
