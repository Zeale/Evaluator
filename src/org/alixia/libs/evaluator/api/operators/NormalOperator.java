package org.alixia.libs.evaluator.api.operators;

import org.alixia.libs.evaluator.api.terms.Term;

public interface NormalOperator<R, F, S> {
	Term<R> evaluate(Term<F> first, Term<S> second);
}
