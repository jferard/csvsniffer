package com.github.jferard.csvsniffer;

import java.util.Collections;
import java.util.List;

public class Sequence {
    private final List<Integer> values;

    public Sequence(List<Integer> values) {
        this.values = values;
    }

    public void clean(int threshold) {
        if (this.values.size() > threshold) {
            int min = Collections.min(this.values);
            int max = Collections.max(this.values);
            this.values.remove((Object) min);
            this.values.remove((Object) max);
        }
    }

    public float score() {
        return this.variance() / this.values.size();
    }

    public int mean() {
        return this.sum() / this.values.size();
    }

    public float variance() {
        float mean = this.mean();
        float var = 0;
        for (int value : this.values) {
            float t = value - mean;
            var += t*t;
        }
        return var / this.values.size();
    }

    public int sum() {
        int s = 0;
        for (int value : this.values) {
            s += value;
        }
        return s;
    }
}
