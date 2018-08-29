package org.alixia.libs.evaluator.api;

import java.util.HashSet;
import java.util.Iterator;

import org.alixia.libs.evaluator.api.VariableMap.Variable;
import org.alixia.libs.evaluator.api.types.BooleanData;
import org.alixia.libs.evaluator.api.types.Data;
import org.alixia.libs.evaluator.api.types.NumericData;

public class VariableMap extends HashSet<Variable<?>> {

	public final Variable<NumericData> PI = new Variable<>("pi", new NumericData(Math.PI)),
			E = new Variable<>("E", new NumericData(Math.E));
	public final Variable<BooleanData> TRUE = new Variable<>("true", new BooleanData(true)),
			FALSE = new Variable<>("false", new BooleanData(false));

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

	public class Variable<T extends Data<?>> {

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
