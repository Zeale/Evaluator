package org.alixia.libs.evaluator.api;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;

import org.alixia.libs.evaluator.api.VariableMap.Variable;

public class VariableMap extends HashSet<Variable<?>> {

	private static final BigDecimal BIG_DECIMAL_PI = new BigDecimal(Math.PI), BIG_DECIMAL_E = new BigDecimal(Math.E);

	public final Variable<BigDecimal> PI = new Variable<>("pi", BIG_DECIMAL_PI), E = new Variable<>("E", BIG_DECIMAL_E);

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public Variable<?> getVariable(String name) {
		for (Variable<?> v : this) {
			if (v.ignoreCase ? v.name.equalsIgnoreCase(name) : v.name.equals(name))
				return v;
			else
				continue;
		}
		return null;
	}

	public final void clearVars() {
		for (Iterator<Variable<?>> iterator = iterator(); iterator.hasNext();) {
			if (iterator.next().modifiable)
				iterator.remove();
		}
	}

	public class Variable<T> {

		private T value;
		private final boolean modifiable, ignoreCase;
		private final String name;

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Variable))
				return false;
			Variable<?> var = (Variable<?>) obj;
			return var.ignoreCase || ignoreCase ? name.equalsIgnoreCase(var.name) : name.equals(var.name);
		}

		{
			add(this);
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
			if (!modifiable)
				throw new RuntimeException("The variable, " + this + ", cannot be modified.");
			this.value = value;
		}

		@Override
		public String toString() {
			return name + "[" + value + "]";
		}

		public boolean isModifiable() {
			return modifiable;
		}

	}

}
