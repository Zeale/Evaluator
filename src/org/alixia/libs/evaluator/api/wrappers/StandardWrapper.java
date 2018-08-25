package org.alixia.libs.evaluator.api.wrappers;

public enum StandardWrapper implements Wrapper {
	PARENTHESES('(', ')'), BRACKETS('[', ']'), BRACES('{', '}'), CHEVRONS('<', '>');

	public static StandardWrapper closeValueOf(final char c) {
		for (final StandardWrapper sw : StandardWrapper.values())
			if (sw.getCloser() == c)
				return sw;
		return null;
	}

	public static StandardWrapper openValueOf(final char c) {
		for (final StandardWrapper sw : StandardWrapper.values())
			if (sw.getOpener() == c)
				return sw;
		return null;
	}

	// TODO Rename to "opener."
	private final char openner, closer;

	private StandardWrapper(final char openner, final char closer) {
		this.openner = openner;
		this.closer = closer;
	}

	@Override
	public char getCloser() {
		return closer;
	}

	@Override
	public char getOpener() {
		return openner;
	}
}
