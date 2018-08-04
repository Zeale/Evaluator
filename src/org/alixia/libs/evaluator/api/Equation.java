package org.alixia.libs.evaluator.api;

import java.util.LinkedList;
import java.util.List;

import org.alixia.libs.evaluator.api.statements.Statement;
import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;

public class Equation<T> implements Term<T> {

	private final List<Statement> assignments = new LinkedList<>();
	private final ChainTerm<T> expression;

	public Equation(ChainTerm<T> expression, Statement... assignments) {
		this.expression = expression;
		for (Statement s : assignments)
			this.assignments.add(s);
	}

	@Override
	public T evaluate() {
		for (Statement s : assignments)
			s.execute();
		return expression.evaluate();
	}

}
