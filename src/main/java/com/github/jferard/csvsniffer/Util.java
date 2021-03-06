package com.github.jferard.csvsniffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util {
    public static byte[] readToBuffer(InputStream is, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int count = is.read(buffer, 0, buffer.length);
        int total = count;
        while (count != -1 && total < buffer.length) {
            count = is.read(buffer, total, buffer.length - total);
            total += count;
        }
        if (total == bufferSize) {
            return buffer;
        }
        byte[] trimmedBuffer = new byte[total];
        System.arraycopy(buffer, 0, trimmedBuffer, 0, total);
        return trimmedBuffer;
    }

    /**
     * Keep order
     * @param values
     * @param <T>
     * @return
     */
    public static <T> List<T> removeDuplicates(List<T> values) {
        List<T> uniqueValues = new ArrayList<T>(values.size());
        Set<T> seen = new HashSet<T>();
        for (T value : values) {
            if (!seen.contains(value)) {
                uniqueValues.add(value);
                seen.add(value);
            }
        }
        return uniqueValues;
    }
}
