package com.github.jferard.csvsniffer;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    private final List<Integer> values;

    public Sequence(List<Integer> values) {
        this.values = new ArrayList<Integer>(values);
    }

    /**
     * If the sequence length is gt the threshold, remove the min and the max
     *
     * @param threshold the threshold
     */
    public void clean(int threshold) {
        if (this.values.size() > threshold) {
            int minIndex = 0;
            int maxIndex = 0;
            int min = this.values.get(0);
            int max = this.values.get(0);
            for (int i = 1; i < this.values.size(); i++) {
                int value = this.values.get(i);
                if (value < min) {
                    min = value;
                    minIndex = i;
                } else if (value > max) {
                    max = value;
                    maxIndex = i;
                }
            }
            if (minIndex < maxIndex) {
                this.values.remove(maxIndex);
                this.values.remove(minIndex);
            } else {
                this.values.remove(minIndex);
                this.values.remove(maxIndex);
            }
        }
    }

    /**
     * @return the score of the sequence
     * @param size the size of the population
     */
    public double score(int size) {
        return this.variance(size) / size;
    }

    /**
     * @return the mean of the sequence
     * @param size the size of the population
     */
    public double mean(int size) {
        return (double) this.sum() / size;
    }

    /**
     * @return the population variance of the sequence
     * @param size the size of the population
     */
    public double variance(int size) {
        double mean = this.mean(size);
        double var = 0.0;
        for (int value : this.values) {
            double t = value - mean;
            var += t * t;
        }
        double squareMean = mean * mean;
        for (int i=this.values.size(); i<size; i++) {
            var += squareMean;
        }
        return var / size;
    }

    /**
     * @return the sum the sequence
     */
    public int sum() {
        int s = 0;
        for (int value : this.values) {
            s += value;
        }
        return s;
    }
}
