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
}
