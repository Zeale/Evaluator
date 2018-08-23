package org.alixia.libs.evaluator.api;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.alixia.libs.evaluator.api.statements.Statement;
import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.Data;

public class Equation<T extends Data<?>> implements Term<T> {

	private final List<Statement> assignments = new LinkedList<>();
	private ChainTerm<T> expression;

	public Equation() {
	}

	public void setExpression(ChainTerm<T> expression) {
		this.expression = expression;
	}

	public void addAssignment(Statement... assignments) {
		for (Statement s : assignments)
			this.assignments.add(s);
	}

	@Override
	public T evaluate() {
		for (Statement s : assignments)
			s.execute();
		return expression.evaluate();
	}

	public ChainTerm<T> getExpression() {
		return expression;
	}

	public List<Statement> getAssignments() {
		return Collections.unmodifiableList(assignments);
	}

}
