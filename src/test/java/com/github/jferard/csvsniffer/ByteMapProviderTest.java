package com.github.jferard.csvsniffer;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

public class ByteMapProviderTest {
	@Test
	public final void test() throws CharacterCodingException {
		ByteMapProvider bmp = new ByteMapProvider();
		char[] cs = bmp.get(Charset.forName("ISO-8859-15"));
		Assert.assertEquals(256, cs.length);
		Assert.assertEquals('é', cs[0xe9]);
	}

}
