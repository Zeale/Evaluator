package org.alixia.libs.evaluator.api.wrappers;

public enum StandardWrapper implements Wrapper {
	PARENTHESES('(', ')'), BRACKETS('[', ']'), BRACES('{', '}'), CHEVRONS('<', '>');

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
}
