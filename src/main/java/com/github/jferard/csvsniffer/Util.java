package com.github.jferard.csvsniffer;

import java.io.IOException;
import java.io.InputStream;

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
}
