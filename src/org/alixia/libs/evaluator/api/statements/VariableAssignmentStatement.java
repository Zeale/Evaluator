package org.alixia.libs.evaluator.api.statements;

import org.alixia.libs.evaluator.api.VariableMap.Variable;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.Data;

public class VariableAssignmentStatement<V extends Data<?>> implements Statement {

	private final Variable<V> variable;
	private final Term<V> value;

	public VariableAssignmentStatement(Variable<V> variable, Term<V> value) {
		this.variable = variable;
		this.value = value;
	}

	@Override
	public void execute() {
		variable.setValue(value.evaluate());
	}

}
