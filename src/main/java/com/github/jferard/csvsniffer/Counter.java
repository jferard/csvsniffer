package com.github.jferard.csvsniffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Counter<T> {
    private final Map<T, Integer> countByElement;

    public Counter() {
        this.countByElement = new HashMap<T, Integer>();
    }

    public Counter(final Map<T, Integer> countByElement) {
        this.countByElement = new HashMap<T, Integer>(countByElement);
    }

    public Counter(final Counter<T> other) {
        this(other.countByElement);
    }

    public void add(final T element, final int count) {
        this.countByElement.put(element, this.get(element)+count);
    }

    public void add(final T element) {
        this.add(element, 1);
    }

    public int get(final T element) {
        final Integer curCount = this.countByElement.get(element);
        if (curCount == null) {
            return 0;
        } else {
            return curCount;
        }
    }

    public void update(final Counter<T> other) {
        for (final Map.Entry<T, Integer> entry : other.countByElement.entrySet()) {
            final T element = entry.getKey();
            final Integer count = entry.getValue();
            this.add(element, count);
        }
    }

    public Set<Map.Entry<T, Integer>> entrySet() {
        return this.countByElement.entrySet();
    }

    public List<T> descKeys() {
        final List<T> elements = new ArrayList<T>(this.countByElement.keySet());
        Collections.sort(elements, new Comparator<T>() {
            @Override
            public int compare(final T c1, final T c2) {
                return Counter.this.countByElement.get(c2) -
                        Counter.this.countByElement.get(c1);
            }
        });
        return elements;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "countByElement=" + this.countByElement +
                '}';
    }

    public T firstOrNull() {
        return this.firstOr(null);
    }

    public T firstOr(final T defaultValue) {
        final Iterator<T> it = this.descKeys().iterator();
        if (it.hasNext()) {
            return it.next();
        } else {
            return defaultValue;
        }
    }

    /**
     * @return true if this counter is empty.
     */
    public boolean isEmpty() {
        return this.countByElement.isEmpty();
    }

    @Override
    public int hashCode() {
        return this.countByElement.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Counter)) {
            return false;
        }
        final Counter<?> other = (Counter<?>) o;
        return this.countByElement.equals(other.countByElement);
    }
}
