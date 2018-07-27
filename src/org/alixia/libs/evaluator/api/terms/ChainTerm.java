package org.alixia.libs.evaluator.api.terms;

import org.alixia.libs.evaluator.api.Chain;
import org.alixia.libs.evaluator.api.operators.NormalOperator;

public class ChainTerm<T> implements Term<T> {

	private final Chain<Term<T>, NormalOperator<T, T, T>> chain;

	public ChainTerm(Term<T> first) {
		if (first == null)
			throw null;
		chain = new Chain<Term<T>, NormalOperator<T, T, T>>(first);
	}

	public void append(NormalOperator<T, T, T> operator, Term<T> term) {
		if (operator == null || term == null)
			throw null;
		chain.append(operator, term);
	}

	@Override
	public String toString() {
		return evaluate().toString();
	}

	@Override
	public T evaluate() {
		// TODO Get more efficient algorithm
		Term<T> value = chain.getFront();
		while (chain.linked()) {
			value = chain.getS(0).evaluate(value, chain.getF(1));
			chain.replaceF(1, value);
			chain.remove(0);
		}
		return value.evaluate();
	}

}
