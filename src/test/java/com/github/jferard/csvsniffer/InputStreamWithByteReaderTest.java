package com.github.jferard.csvsniffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InputStreamWithByteReaderTest {
	private Charset iso;
	private char[] cs;
	private char[] cbuf;
	private char[] cbuf2;

	@Before
	public void setUp() {
		ByteMapProvider bmp = new ByteMapProvider();
		this.iso = Charset.forName("ISO-8859-15");
		this.cs = bmp.get(this.iso);
		this.cbuf = new char[100];
	}

	@Test
	public final void test() throws IOException {
		final String s = "&éè-_-ç";
		InputStreamWithByteCharset r = this.getReaderFromString(s, this.iso);
		Assert.assertEquals(r.read(null, this.cbuf, 0, 7), 7);
		for (char c = 0; c<7; c++) {
			Assert.assertEquals(this.cbuf[c], s.toCharArray()[c]);
		}
	}

	public InputStreamWithByteCharset getReaderFromString(final String s,
			final Charset cs) {
		final ByteArrayInputStream is = new ByteArrayInputStream(
				s.getBytes(cs));
		return new InputStreamWithByteCharset(is, this.cs);
	}
}
