package org.alixia.libs.evaluator.api.statements;

import org.alixia.libs.evaluator.api.Variable;
import org.alixia.libs.evaluator.api.terms.Term;

public class VariableAssignmentStatement<V> implements Statement {

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
