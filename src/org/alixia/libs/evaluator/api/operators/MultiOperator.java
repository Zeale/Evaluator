package org.alixia.libs.evaluator.api.operators;

import java.util.LinkedList;
import java.util.List;

import org.alixia.libs.evaluator.api.terms.ChainTerm;

public class MultiOperator implements Operator {
	private final NormalOperator combiningOperator;
	private final List<Operator> otherOperators = new LinkedList<>();

	public MultiOperator(NormalOperator combiningOperator, Operator... otherOperators) {
		this.combiningOperator = combiningOperator;
		for (Operator o : otherOperators)
			this.otherOperators.add(o);
	}

	public void addOperator(Operator operator) {
		if (!otherOperators.contains(operator))
			otherOperators.add(operator);
	}

	@Override
	public void evaluate(ChainTerm<?>.MathChain chain, ChainTerm<?>.MathChain.MathIterator iterator) {
		for (Operator o : otherOperators)
			o.evaluate(chain, iterator);
		combiningOperator.evaluate(chain, iterator);
	}

}
