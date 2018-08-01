package org.alixia.libs.evaluator.api;

public class Variable<T> {

	private T value;
	private final boolean modifiable;

	public Variable(T value) {
		this.value = value;
		modifiable = false;
	}

	public Variable(T value, boolean modifiable) {
		this.value = value;
		this.modifiable = modifiable;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		// TODO EXCEPTION
		this.value = value;
	}

	public boolean isModifiable() {
		return modifiable;
	}

}
