package com.github.jferard.csvsniffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * A BOMSniffer creates a ByteArraysMatcher that tests the beginning of a
 * stream.
 */
public class BOMSniffer {
	private static final byte FF = (byte) 0xFF;
	private static final byte FE = (byte) 0xFE;
	private static final byte BB = (byte) 0xBB;
	private static final byte BF = (byte) 0xBF;
	private static final byte EF = (byte) 0xEF;
	private static final byte ZERO = (byte) 0x00;
	
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
	public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
	public static final Charset UTF_32BE = Charset.forName("UTF-32BE");
	public static final Charset UTF_32LE = Charset.forName("UTF-32LE");

	public static final byte[] UTF_8_BOM = new byte[] { EF, BB, BF };
	public static final byte[] UTF_16BE_BOM = new byte[] { FE, FF };
	public static final byte[] UTF_16LE_BOM = new byte[] { FF, FE };
	public static final byte[] UTF_32BE_BOM = new byte[] { ZERO, ZERO, FE, FF };
	public static final byte[] UTF_32LE_BOM = new byte[] { FF, FE, ZERO, ZERO };

	private final Map<byte[], Charset> charsetByBOM;

	/**
	 * Detect a Charset BOM in an InputStream. If a BOM is found, it is skipped.
	 * @param is the InputStream. Must support mark
	 * @return the charset found from BOM, null if none
	 * @throws IOException
	 */
	public static Charset getCharset(InputStream is) throws IOException {
		if (!is.markSupported())
			throw new IllegalArgumentException();
		
		BOMSniffer sniffer = BOMSniffer.create();
		ByteArraysMatcher matcher = sniffer.createMatcher(is);
		is.mark(matcher.getRemainingByteArraysMaxLength());
		byte[] bom = matcher.longestMatch();
		is.reset();
		if (bom != null) {
			for (int i=0; i<bom.length; i++)
				is.read();
		}
		return sniffer.getCharset(bom);
	}

	public static BOMSniffer create() {
		final Map<byte[], Charset> charsetByBOM = new HashMap<byte[], Charset>();
		charsetByBOM.put(UTF_8_BOM, UTF_8);
		charsetByBOM.put(UTF_16BE_BOM, UTF_16BE);
		charsetByBOM.put(UTF_16LE_BOM, UTF_16LE);
		charsetByBOM.put(UTF_32BE_BOM, UTF_32BE);
		charsetByBOM.put(UTF_32LE_BOM, UTF_32LE);
		return new BOMSniffer(charsetByBOM);
	}

	public BOMSniffer(Map<byte[], Charset> charsetByBOM) {
		this.charsetByBOM = charsetByBOM;
	}

	public ByteArraysMatcher createMatcher(InputStream is) {
		return new ByteArraysMatcher(charsetByBOM.keySet(), is);
	}

	private Charset getCharset(byte[] bom) {
		return this.charsetByBOM.get(bom);
	}
}