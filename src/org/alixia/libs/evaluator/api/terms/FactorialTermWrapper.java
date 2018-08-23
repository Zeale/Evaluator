package org.alixia.libs.evaluator.api.terms;

import org.alixia.libs.evaluator.api.functions.SimpleFunction;
import org.alixia.libs.evaluator.api.types.NumericData;

public class FactorialTermWrapper implements Term<NumericData> {

	private final Term<NumericData> term;

	public FactorialTermWrapper(Term<NumericData> term) {
		this.term = term;
	}

	@Override
	public NumericData evaluate() {
		try {
			return SimpleFunction.FACTORIAL.evaluate(term.evaluate()).evaluate();
		} catch (ClassCastException e) {
			throw new RuntimeException("The factorial operator was illegally applied to a term. Term: " + term
					+ ", Term Value: " + term.evaluate());
		}
	}

}
