package org.alixia.libs.evaluator.api;

public class Number<T extends java.lang.Number> implements Term<T> {
	private final T value;

	public Number(T value) {
		if (value == null)
			throw new IllegalArgumentException("Numbes can't have null values.");
		this.value = value;
	}

	@Override
	public T evaluate() {
		return value;
	}

}
