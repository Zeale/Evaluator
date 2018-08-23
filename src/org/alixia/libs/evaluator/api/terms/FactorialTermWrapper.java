package org.alixia.libs.evaluator.api.terms;

import java.math.BigDecimal;

import org.alixia.libs.evaluator.api.functions.SimpleFunction;

public class FactorialTermWrapper implements Term<BigDecimal> {

	private final Term<BigDecimal> term;

	public FactorialTermWrapper(Term<BigDecimal> term) {
		this.term = term;
	}

	@Override
	public BigDecimal evaluate() {
		try {
			return new BigDecimal(SimpleFunction.FACTORIAL.evaluate(term.evaluate().toBigInteger()).evaluate());
		} catch (ClassCastException e) {
			throw new RuntimeException("The factorial operator was illegally applied to a term. Term: " + term
					+ ", Term Value: " + term.evaluate());
		}
	}

}
