package com.github.jferard.csvsniffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BytesArrayMatcherTest {

	@Before
	public void setUp() {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptySet() throws IOException {
		ByteArraysMatcher bam = new ByteArraysMatcher(
				Collections.<byte[]> emptySet(), this.getInputStream("",
						BOMSniffer.UTF_8));
		bam.shortestMatch();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyByteArray() throws IOException {
		Set<byte[]> s = new HashSet<byte[]>();
		s.add(new byte[] {});
		ByteArraysMatcher bam = new ByteArraysMatcher(s, this.getInputStream(
				"", BOMSniffer.UTF_8));
		Assert.assertEquals(null, bam.shortestMatch());
	}

	@Test
	public void testEmptyString() throws IOException {
		Set<byte[]> s = new HashSet<byte[]>();
		s.add(new byte[] { 'a' });
		ByteArraysMatcher bam = new ByteArraysMatcher(s, this.getInputStream(
				"", BOMSniffer.UTF_8));
		Assert.assertEquals(null, bam.shortestMatch());
	}

	@Test
	public void testMatchingString() throws IOException {
		Set<byte[]> s = new HashSet<byte[]>();
		byte[] bs = new byte[] { 'a' };
		s.add(bs);
		ByteArraysMatcher bam = new ByteArraysMatcher(s, this.getInputStream(
				"a", BOMSniffer.UTF_8));
		Assert.assertArrayEquals(bs, bam.shortestMatch());
	}

	@Test
	public void testMatchingString2() throws IOException {
		Set<byte[]> s = new HashSet<byte[]>();
		byte[] bs = new byte[] { 'a' };
		byte[] bs2 = new byte[] { 'a', 'a' };
		s.add(bs);
		s.add(bs2);
		ByteArraysMatcher bam = new ByteArraysMatcher(s, this.getInputStream(
				"aa", BOMSniffer.UTF_8));
		Assert.assertArrayEquals(bs, bam.shortestMatch());
	}
	
	@Test
	public void testMatchingString3() throws IOException {
		Set<byte[]> s = new HashSet<byte[]>();
		byte[] bs = new byte[] { 'a' };
		byte[] bs2 = new byte[] { 'a', 'a' };
		s.add(bs);
		s.add(bs2);
		ByteArraysMatcher bam = new ByteArraysMatcher(s, this.getInputStream(
				"aaTEST", BOMSniffer.UTF_8));
		Assert.assertArrayEquals(bs2, bam.longestMatch());
	}

	@Test
	public void testNotMatchingString() throws IOException {
		Set<byte[]> s = new HashSet<byte[]>();
		byte[] bs = new byte[] { 'a' };
		byte[] bs2 = new byte[] { 'a', 'a' };
		s.add(bs);
		s.add(bs2);
		ByteArraysMatcher bam = new ByteArraysMatcher(s, this.getInputStream(
				"b", BOMSniffer.UTF_8));
		Assert.assertNull(bam.shortestMatch());
	}

	InputStream getInputStream(String s, Charset cs) {
		byte[] bytes = s.getBytes(cs);
		return new ByteArrayInputStream(bytes);
	}

}
