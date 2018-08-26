package org.alixia.libs.evaluator.api.operators;

import org.alixia.libs.evaluator.api.terms.ChainTerm;

public interface Operator {
	void evaluate(ChainTerm<?>.MathChain chain, int position, ChainTerm<?>.MathChain.MathIterator iterator);
}
