package org.alixia.libs.evaluator.api.operators;

import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.Data;

public interface SimpleOperator<R extends Data<?>> extends Operator {

	public Term<R> operate(Term<?> inputTerm);

	@Override
	default void evaluate(ChainTerm<?>.MathChain chain, ChainTerm<?>.MathChain.MathIterator iterator) {
		ChainTerm<?>.MathChain.Pair current = iterator.current();
		current.setFirst(operate(current.getFirst()));
	}
}
