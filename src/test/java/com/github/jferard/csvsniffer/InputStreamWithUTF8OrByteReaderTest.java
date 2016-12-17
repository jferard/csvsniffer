package com.github.jferard.csvsniffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InputStreamWithUTF8OrByteReaderTest {

	private Charset iso;
	private char[] cs;
	private char[] cbuf;

	@Before
	public void setUp() {
		ByteMapProvider bmp = new ByteMapProvider();
		this.iso = Charset.forName("ISO-8859-15");
		this.cbuf = new char[100];
		this.cs = bmp.get(this.iso);
	}

	@Test
	public final void testIsoFalse3Bytes() throws IOException {
		final String s = "é";
		InputStreamUTF8OrByteCharsetReader r = this.getReaderFromString(s,
				this.iso);
		Assert.assertEquals(1, r.read(this.cbuf, 0, 7));
		Assert.assertEquals("é", new String(this.cbuf, 0, 1));
	}

	public InputStreamUTF8OrByteCharsetReader getReaderFromString(final String s,
			final Charset cs) throws IOException {
		final ByteArrayInputStream is = new ByteArrayInputStream(
				s.getBytes(cs));
		return new InputStreamUTF8OrByteCharsetReader(is, this.cs);
	}
}
