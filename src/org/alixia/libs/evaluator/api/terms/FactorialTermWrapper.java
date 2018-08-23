package org.alixia.libs.evaluator.api.terms;

import org.alixia.libs.evaluator.api.functions.SimpleFunction;

public class FactorialTermWrapper implements Term<Double> {

	private final Term<?> term;

	public FactorialTermWrapper(Term<?> term) {
		this.term = term;
	}

	@Override
	public Double evaluate() {
		try {
			return SimpleFunction.FACTORIAL.evaluate(((java.lang.Number) term.evaluate())).evaluate();
		} catch (ClassCastException e) {
			throw new RuntimeException("The factorial operator was illegally applied to a term. Term: " + term
					+ ", Term Value: " + term.evaluate());
		}
	}

}
