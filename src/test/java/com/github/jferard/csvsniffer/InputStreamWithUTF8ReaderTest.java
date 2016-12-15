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

	@Before
	public void setUp() throws Exception {
		this.utf8 = Charset.forName("UTF-8");
		this.iso = Charset.forName("ISO-8859-15");
	}

	@Test
	public final void test() throws IOException {
		final String s = "&éè-_-ç";
		final ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes(this.utf8));
		InputStreamWithUTF8Reader r = new InputStreamWithUTF8Reader(is);
		char[] cbuf = new char[7];
		Assert.assertEquals(r.read(null, cbuf, 0, 7), 7);
		Assert.assertEquals(new String(s.toCharArray()), new String(cbuf));
	}

	@Test
	public final void testIsoFalse3Bytes() throws IOException {
		InputStreamWithUTF8OrByteReader p = PowerMock.createMock(InputStreamWithUTF8OrByteReader.class);
		char[] cbuf = new char[7];
		
		// play
		p.fall();
		EasyMock.expect(p.read(cbuf, 0, 7)).andReturn(65);
		
		PowerMock.replayAll();
		final String s = "é";
		final ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes(this.iso));
		InputStreamWithUTF8Reader r = new InputStreamWithUTF8Reader(is);
		Assert.assertEquals(r.read(p, cbuf, 0, 7), 65);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testIsoButProcessedAsUTF() throws IOException {
		final String s = "Ã©";
		final ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes(this.iso));
		InputStreamWithUTF8Reader r = new InputStreamWithUTF8Reader(is);
		char[] cbuf = new char[7];
		Assert.assertEquals(r.read(null, cbuf, 0, 7), 1);
		Assert.assertEquals("é", new String(cbuf, 0, 1));
	}
}
