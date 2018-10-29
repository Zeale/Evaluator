package org.alixia.libs.evaluator.api.types;

public abstract class SimpleData<T> implements Data<T> {
	protected T value;

	public SimpleData(T value) {
		this.value = value;
	}

	@Override
	public T evaluate() {
		return value;
	}

	@Override
	public abstract SimpleData<T> clone();

	@Override
	public final String toString() {
		return toStringValue();
	}

	@Override
	public boolean isEqualTo(Data<?> other) {
		return other instanceof SimpleData<?> ? value.equals(((SimpleData<?>) other).value)
				: Data.super.isEqualTo(other);
	}

}
