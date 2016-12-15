package com.github.jferard.csvsniffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ByteMapProvider {
	public ByteMapProvider() {}
	
	char[] get(Charset charset) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(256);
		for (int c = 0; c<256; c++) {
			byteBuffer.put((byte) c);
		}
		return new String(byteBuffer.array(), charset).toCharArray();
	}

}
