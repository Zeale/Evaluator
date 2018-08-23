package org.alixia.libs.evaluator.api.operators;

import org.alixia.libs.evaluator.api.terms.Term;

public interface NormalOperator {
	Term<?> evaluate(Term<?> first, Term<?> second);
}
