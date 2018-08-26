package org.alixia.libs.evaluator.api.operators;

import org.alixia.libs.evaluator.api.Chain;
import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;

public interface Operator {
	void evaluate(ChainTerm<?> equation, int position, Chain<Term<?>, Operator>.ChainIterator iterator);
}
