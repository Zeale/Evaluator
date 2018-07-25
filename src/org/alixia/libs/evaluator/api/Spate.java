package org.alixia.libs.evaluator.api;

import java.util.Iterator;

public interface Spate<O> {

	public abstract O next();

	public abstract boolean hasNext();

	public static Spate<Character> spate(CharSequence sequence) {
		return new Spate<Character>() {

			private int pos;

			@Override
			public Character next() {
				return hasNext() ? sequence.charAt(pos++) : null;
			}

			@Override
			public boolean hasNext() {
				return pos < sequence.length();
			}
		};
	}

	public static <O> Spate<O> spate(Iterator<O> iterator) {
		return new Spate<O>() {

			@Override
			public O next() {
				return iterator.next();
			}

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}
		};
	}

	public static <O> Spate<O> spate(Iterable<O> iterable) {
		return spate(iterable.iterator());
	}

}
