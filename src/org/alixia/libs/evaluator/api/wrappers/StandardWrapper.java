package org.alixia.libs.evaluator.api.wrappers;

public enum StandardWrapper implements Wrapper {
	PARENTHESES('(', ')'), BRACKETS('[', ']'), BRACES('{', '}'), CHEVRONS('<', '>');

	// TODO Rename to "opener."
	private final char openner, closer;

	private StandardWrapper(char openner, char closer) {
		this.openner = openner;
		this.closer = closer;
	}

	@Override
	public char getOpenner() {
		return openner;
	}

	@Override
	public char getCloser() {
		return closer;
	}

	public static StandardWrapper openValueOf(char c) {
		for (StandardWrapper sw : StandardWrapper.values())
			if (sw.getOpenner() == c)
				return sw;
		return null;
	}

	public static StandardWrapper closeValueOf(char c) {
		for (StandardWrapper sw : StandardWrapper.values())
			if (sw.getCloser() == c)
				return sw;
		return null;
	}
}
