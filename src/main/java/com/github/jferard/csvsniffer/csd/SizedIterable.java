package com.github.jferard.csvsniffer.csd;

import java.util.Collection;
import java.util.Iterator;

/**
* An Iterable with a size.
 */
interface SizedIterable<E> extends Iterable<E> {
    int size();
}
