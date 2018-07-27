package org.alixia.libs.evaluator.api.terms;

public class Number<T extends java.lang.Number> implements Term<T> {
	private final T value;

	public Number(T value) {
		if (value == null)
			throw new IllegalArgumentException("Numbers can't have null values.");
		this.value = value;
	}

	@Override
	public T evaluate() {
		return value;
	}

	@Override
	public String toString() {
		return evaluate().toString();
	}

}
