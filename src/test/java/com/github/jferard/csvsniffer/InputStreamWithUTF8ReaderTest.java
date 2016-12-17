package com.github.jferard.csvsniffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class InputStreamWithUTF8ReaderTest {
	private Charset utf8;
	private Charset iso;
	private char[] cbuf;

	@Before
	public void setUp() throws Exception {
		this.utf8 = Charset.forName("UTF-8");
		this.iso = Charset.forName("ISO-8859-15");
		this.cbuf = new char[100];
	}

	@Test
	public final void testWithZeroToSevenChars() throws IOException {
		final String s = "&éè-_-ç";
		for (int i = 0; i <= 7; i++) {
			InputStreamWithUTF8Reader r = this.getReaderFromString(s,
					this.utf8);
			Assert.assertEquals(i, r.read(null, this.cbuf, 0, i));
			Assert.assertEquals(new String(s.toCharArray()).substring(0, i),
					new String(this.cbuf, 0, i));
		}
	}

	@Test
	public final void testWithLongRead() throws IOException {
		final String s = "&éè-_-ç";
		InputStreamWithUTF8Reader r = this.getReaderFromString(s, this.utf8);
		Assert.assertEquals(7, r.read(null, this.cbuf, 0, 100));
		Assert.assertEquals(new String(s.toCharArray()),
				new String(this.cbuf, 0, 7));
	}

	@Test
	public final void testIsoFalse3Bytes() throws IOException {
		InputStreamWithUTF8OrByteReader p = PowerMock
				.createMock(InputStreamWithUTF8OrByteReader.class);

		// play
		p.fall();
		EasyMock.expect(p.read(this.cbuf, 0, 7)).andReturn(65);

		PowerMock.replayAll();
		final String s = "é";
		InputStreamWithUTF8Reader r = this.getReaderFromString(s, this.iso);
		Assert.assertEquals(r.read(p, this.cbuf, 0, 7), 65);
		PowerMock.verifyAll();
	}

	@Test
	public final void testIsoButProcessedAsUTF() throws IOException {
		final String s = "Ã©";
		InputStreamWithUTF8Reader r = this.getReaderFromString(s, this.iso);
		Assert.assertEquals(r.read(null, this.cbuf, 0, 7), 1);
		Assert.assertEquals("é", new String(this.cbuf, 0, 1));
	}

	public InputStreamWithUTF8Reader getReaderFromString(final String s,
			final Charset cs) throws IOException {
		final ByteArrayInputStream is = new ByteArrayInputStream(
				s.getBytes(cs));
		return new InputStreamWithUTF8Reader(is);
	}
}
