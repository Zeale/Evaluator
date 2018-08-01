package org.alixia.libs.evaluator.api;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Variable<T> {

	private static final Set<Variable<?>> variables = new HashSet<>();

	public static final Variable<Double> PI = new Variable<>("pi", Math.PI), E = new Variable<>("E", Math.E);

	private T value;
	private final boolean modifiable, ignoreCase;
	private final String name;

	public static Variable<?> getVariable(String name) {
		for (Variable<?> v : variables)
			if (v.ignoreCase)
				if (v.name.equalsIgnoreCase(name))
					return v;
				else
					;
			else if (v.name.equals(name))
				return v;
		return null;
	}

	public static final void clearVars() {
		for (Iterator<Variable<?>> iterator = variables.iterator(); iterator.hasNext();) {
			if (iterator.next().modifiable)
				iterator.remove();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Variable))
			return false;
		Variable<?> var = (Variable<?>) obj;
		return var.ignoreCase || ignoreCase ? name.equalsIgnoreCase(var.name) : name.equals(var.name);
	}

	{
		variables.add(this);
	}

	public Variable(String name, T value) {
		this.value = value;
		modifiable = false;
		ignoreCase = true;
		this.name = name;
	}

	public Variable(String name, T value, boolean modifiable) {
		this.value = value;
		this.modifiable = modifiable;
		ignoreCase = false;
		this.name = name;
	}

	public Variable(String name, T value, boolean modifiable, boolean ignoreCase) {
		this.value = value;
		this.modifiable = modifiable;
		this.ignoreCase = ignoreCase;
		this.name = name;
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
