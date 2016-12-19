package com.github.jferard.csvsniffer;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;

class StringUtils {
	final static Pattern p = Pattern
			.compile("\\p{InCombiningDiacriticalMarks}+");

	public static String normalize(String s) {
		String decomposed = java.text.Normalizer.normalize(s.trim(),
				Normalizer.Form.NFD);
		return p.matcher(decomposed) // $NON-NLS-1$
				.replaceAll("").trim();
	}

	/**
	 * see https://github.com/richmilne/JaroWinkler/blob/master/jaro/strcmp95.c
	 */
	public static double strcmp95(final String first, final String second) {
		if (first == null || second == null)
			throw new IllegalArgumentException();
		return StringUtils.strcmp95Normalized(StringUtils.normalize(first).toLowerCase(),
				StringUtils.normalize(second).toLowerCase());
	}

	// TODO
	public static double strcmp95Normalized(String first, String second) {
		return 0;
	}
}
