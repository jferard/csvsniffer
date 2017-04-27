package com.github.jferard.csvsniffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

public class BOMSnifferTest {
	@Test
	public void testUTF16LE() throws IOException {
		this.test_aux(BOMSniffer.UTF_16LE, BOMSniffer.UTF_16LE_BOM);
	}

	@Test
	public void testUTF32LE() throws IOException {
		this.test_aux(BOMSniffer.UTF_32LE, BOMSniffer.UTF_32LE_BOM);
	}
	
	@Test
	public void testUTF16BE() throws IOException {
		this.test_aux(BOMSniffer.UTF_16BE, BOMSniffer.UTF_16BE_BOM);
	}

	@Test
	public void testUTF8() throws IOException {
		this.test_aux(BOMSniffer.UTF_8, BOMSniffer.UTF_8_BOM);
	}

	private void test_aux(Charset cs, byte[] bom) throws IOException {
		InputStream is = this.getInputStream("test", cs, bom);
		Assert.assertEquals(cs, BOMSniffer.getCharset(is));
		Assert.assertEquals("test", this.getString(is, cs));
	}
	
	InputStream getInputStream(String s, Charset cs, byte[] bom) {
		byte[] bytes = s.getBytes(cs);
     	int bomLen = bom.length;
		int bytesLen = bytes.length;
     	byte[] newBytes = new byte[bomLen+bytesLen];
        System.arraycopy(bom, 0, newBytes, 0, bomLen);
        System.arraycopy(bytes, 0, newBytes, bomLen, bytesLen);
		return new ByteArrayInputStream(newBytes);
	}
	
	String getString(InputStream is, Charset cs) throws IOException {
		Reader reader = new InputStreamReader(is, cs);
		StringBuilder builder = new StringBuilder();
		int charsRead = -1;
		char[] chars = new char[100];
		do{
		    charsRead = reader.read(chars,0,chars.length);
		    if(charsRead>0)
		        builder.append(chars,0,charsRead);
		}while(charsRead>0);
		String result = builder.toString();		
		return result;		
	}
}
