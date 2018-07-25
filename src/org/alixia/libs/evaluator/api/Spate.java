package org.alixia.libs.evaluator.api;

public interface Spate<O> {

	O next();

	boolean hasNext();

	O peek();

	void skip();

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

}
