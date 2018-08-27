package org.alixia.libs.evaluator.api.operators;

import java.util.LinkedList;
import java.util.List;

import org.alixia.libs.evaluator.api.terms.ChainTerm;

public class MultiOperator implements Operator, Precedented {
	private NormalOperator combiningOperator;
	private final List<Operator> otherOperators = new LinkedList<>();

	public MultiOperator() {
	}

	public void setCombiningOperator(NormalOperator combiningOperator) {
		this.combiningOperator = combiningOperator;
	}

	public void addOperators(Operator... operators) {
		for (Operator o : operators)
			if (!otherOperators.contains(o))
				otherOperators.add(o);
	}

	@Override
	public void evaluate(ChainTerm<?>.MathChain chain, ChainTerm<?>.MathChain.MathIterator iterator) {
		for (Operator o : otherOperators)
			o.evaluate(chain, iterator);
		combiningOperator.evaluate(chain, iterator);
	}

	@Override
	public Precedence precedence() {
		return combiningOperator instanceof Precedented ? ((Precedented) combiningOperator).precedence()
				: Precedence.NONE;
	}

	@Override
	public String toString() {
		return "MultiOp[" + combiningOperator + "]";
	}

}
