package org.alixia.libs.evaluator.api.operators;

import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;

public interface NormalOperator extends Operator {
	Term<?> evaluate(Term<?> first, Term<?> second);

	@Override
	default void evaluate(ChainTerm<?>.MathChain chain, ChainTerm<?>.MathChain.MathIterator iterator) {
		iterator.skip();
		iterator.combineCurrentAndPreviousWithNormalOp(this);// This method (in iterator) ends up calling
																// evaluate(Term<?>,Term<?>);
		iterator.skipBack();
	}
}
