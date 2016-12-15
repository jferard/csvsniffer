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

	@Before
	public void setUp() {
		ByteMapProvider bmp = new ByteMapProvider();
		this.iso = Charset.forName("ISO-8859-15");
		this.cs = bmp.get(this.iso);
	}

	@Test
	public final void testIsoFalse3Bytes() throws IOException {
		final String s = "é";
		final ByteArrayInputStream is = new ByteArrayInputStream(
				s.getBytes(this.iso));
		Reader r = new InputStreamWithUTF8OrByteReader(is, this.cs);
		char[] cbuf = new char[7];
		Assert.assertEquals(r.read(cbuf, 0, 7), 1);
		Assert.assertEquals("é", new String(cbuf, 0, 1));
	}

}
