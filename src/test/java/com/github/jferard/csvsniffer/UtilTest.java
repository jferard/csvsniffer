package com.github.jferard.csvsniffer;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testRemoveDuplicates() {
        assertEquals(Arrays.asList(0, 1, 2, 3), Util.removeDuplicates(Arrays.asList(0, 1, 2, 3, 0, 0)));
    }

    @Test
    public void testBadRemoveDuplicates() {
        assertEquals(Arrays.asList(0, 10, 2, 3), Util.removeDuplicates(Arrays.asList(0, 10, 10, 0, 10, 2, 10, 2, 2, 3, 0, 3, 0)));
    }

    @Test
    public void testReadToBuffer() throws IOException {
        final byte[] ret = Util.readToBuffer(new ByteArrayInputStream("abcdefghij".getBytes("UTF-8")), 5);
        assertArrayEquals("abcde".getBytes("UTF-8"), ret);
    }

    @Test
    public void testReadToBufferTooLong() throws IOException {
        final byte[] ret = Util.readToBuffer(new ByteArrayInputStream("abcdefghij".getBytes("UTF-8")), 120);
        assertArrayEquals("abcdefghij".getBytes("UTF-8"), ret);
    }

    @Test
    public void testReadToBuffer2() throws IOException {
        final InputStream is = PowerMock.createMock(InputStream.class);

        PowerMock.resetAll();
        EasyMock.expect(is.read(EasyMock.isA(byte[].class), EasyMock.eq(0), EasyMock.eq(10))).andReturn(6);
        EasyMock.expect(is.read(EasyMock.isA(byte[].class), EasyMock.eq(6), EasyMock.eq(4))).andReturn(2);
        EasyMock.expect(is.read(EasyMock.isA(byte[].class), EasyMock.eq(8), EasyMock.eq(2))).andReturn(2);

        PowerMock.replayAll();
        Util.readToBuffer(is, 10);

        PowerMock.verifyAll();
    }

    @Test
    public void testReadToBuffer3() throws IOException {
        final InputStream is = PowerMock.createMock(InputStream.class);

        PowerMock.resetAll();
        EasyMock.expect(is.read(EasyMock.isA(byte[].class), EasyMock.eq(0), EasyMock.eq(10))).andReturn(6);
        EasyMock.expect(is.read(EasyMock.isA(byte[].class), EasyMock.eq(6), EasyMock.eq(4))).andReturn(2);
        EasyMock.expect(is.read(EasyMock.isA(byte[].class), EasyMock.eq(8), EasyMock.eq(2))).andReturn(-1);

        PowerMock.replayAll();
        Util.readToBuffer(is, 10);

        PowerMock.verifyAll();
    }}