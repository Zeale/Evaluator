package org.alixia.libs.evaluator.api.operators;

import org.alixia.libs.evaluator.api.terms.ChainTerm;

public interface Operator {
	void evaluate(ChainTerm<?>.MathChain chain, ChainTerm<?>.MathChain.MathIterator iterator);
}
