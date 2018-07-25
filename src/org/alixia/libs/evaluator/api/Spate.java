package org.alixia.libs.evaluator.api;

public interface Spate<O> {

	public abstract O next();

	public abstract boolean hasNext();

	public abstract O peek();

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
		};
	}

}
