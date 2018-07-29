package org.alixia.libs.evaluator.api.functions;

public class Argument<T> {

	@SuppressWarnings("rawtypes")
	public static final Argument NOT_USED = new Argument();

	private final T value;

	public T getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	private Argument() {
		value = (T) this;
	}

	public Argument(T value) {
		this.value = value;
	}

}
