package com.github.jferard.csvsniffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter<T> {
	private Map<T, Integer> countByElement;

	public Counter() {
		this.countByElement = new HashMap<T, Integer>();
	}

	public void put(T element) {
		Integer count = this.countByElement.get(element);
		if (count == null)
			count = 0;

		this.countByElement.put(element, count + 1);
	}

	public List<T> sortedElements() {
		List<T> l = new ArrayList<T>(this.countByElement.keySet());
		final Map<T, Integer> closedCountByElements = this.countByElement;
		Collections.sort(l, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return closedCountByElements.get(o1)
						- closedCountByElements.get(o2);
			}
		});
		return l;
	}

	public T maxElementOr(T defaultElement) {
		List<T> l = this.sortedElements();
		if (l.isEmpty())
			return defaultElement;
		else
			return l.get(0);
	}

}
