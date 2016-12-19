package com.github.jferard.csvsniffer;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HeaderRowAnalyzerTest {

	private HeaderRowAnalyzer h;

	@Before
	public void setUp() throws Exception {
		this.h = new HeaderRowAnalyzer();
	}

	@Test
	public final void test() throws IOException {
		CSVFormat format = this.h.analyze(Arrays.asList("a", "b"), "a;b;c");
		Assert.assertEquals(';', format.getDelimiter());
		format = this.h.analyze(Arrays.asList("a", "b"), "a,b,c");
		Assert.assertEquals(',', format.getDelimiter());
	}
	
	@Test
	public final void test2() throws IOException {
		CSVFormat format = this.h.analyze(Arrays.asList("a'b", "b"), "'a&'b';b;c");
		Assert.assertEquals(';', format.getDelimiter());
		Assert.assertEquals('&', (char) format.getEscapeCharacter());
		Assert.assertEquals('\'', (char) format.getQuoteCharacter());
	}

}
