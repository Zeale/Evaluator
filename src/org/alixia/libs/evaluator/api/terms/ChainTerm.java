package org.alixia.libs.evaluator.api.terms;

import org.alixia.libs.evaluator.api.Chain;
import org.alixia.libs.evaluator.api.operators.NormalOperator;

public class ChainTerm<T> implements Term<T> {

	private final Chain<Term<? extends T>, NormalOperator<? extends T, ? super T, ? super T>> chain;

	public ChainTerm(Term<T> first) {
		chain = new Chain<Term<? extends T>, NormalOperator<? extends T, ? super T, ? super T>>(first);
	}

	public void append(NormalOperator<? extends T, ? super T, ? super T> operator, Term<? extends T> term) {
		chain.append(operator, term);
	}

	@Override
	public T evaluate() {
		// TODO Implement
		return null;
	}

}
