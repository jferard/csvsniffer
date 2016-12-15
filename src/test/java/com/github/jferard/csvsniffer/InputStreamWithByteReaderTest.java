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

	@Before
	public void setUp() {
		ByteMapProvider bmp = new ByteMapProvider();
		this.iso = Charset.forName("ISO-8859-15");
		this.cs = bmp.get(this.iso);
	}
	
	@Test
	public final void test() throws IOException {
		final String s = "&éè-_-ç";
		final ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes(this.iso));
		InputStreamWithByteReader r = new InputStreamWithByteReader(is, this.cs);
		char[] cbuf = new char[7];
		Assert.assertEquals(r.read(null, cbuf, 0, 7), 7);
		Assert.assertArrayEquals(cbuf, s.toCharArray());
	}

}
