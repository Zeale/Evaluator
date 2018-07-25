package org.alixia.libs.evaluator.api;

public interface NormalOperator<R,F,S> {
	public Term<R> evaluate(Term<F> first, Term<S> second);
}
