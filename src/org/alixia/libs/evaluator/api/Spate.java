package org.alixia.libs.evaluator.api;

public interface Spate<O> {

	public static Spate<Character> spate(final CharSequence sequence) {
		return new Spate<Character>() {

			private int pos = -1;

			@Override
			public boolean hasNext() {
				return pos + 1 < sequence.length();
			}

			@Override
			public Character next() {
				return hasNext() ? sequence.charAt(++pos) : null;
			}

			@Override
			public Character peek() {
				return hasNext() ? sequence.charAt(pos + 1) : null;
			}

			@Override
			public void skip() {
				pos++;
			}
		};
	}

	boolean hasNext();

	O next();

	O peek();

	void skip();

}
