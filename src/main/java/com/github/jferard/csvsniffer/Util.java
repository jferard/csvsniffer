package com.github.jferard.csvsniffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util {
    /**
     * Read bufferSize bytes of an input stream
     *
     * @param is         the input stream
     * @param bufferSize the size
     * @return a byte array
     * @throws IOException if an I/O error occurs
     */
    public static byte[] readToBuffer(InputStream is, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int count = is.read(buffer, 0, buffer.length);
        int total = count;
        while (total < buffer.length) {
            count = is.read(buffer, total, buffer.length - total);
            if (count == -1) {
                break;
            }
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
     *
     * @param values the values
     * @param <T>    the type of the values
     * @return a list of values wo duplicates
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
