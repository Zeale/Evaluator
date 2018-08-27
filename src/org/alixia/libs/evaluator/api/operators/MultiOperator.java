package org.alixia.libs.evaluator.api.operators;

import java.util.LinkedList;
import java.util.List;

import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.Data;

public class MultiOperator implements Operator, Precedented {
	private NormalOperator combiningOperator;
	private final List<Operator> otherOperators = new LinkedList<>();

	public static final NormalOperator NULL_DELETE_OPERATOR = new NormalOperator() {

		@Override
		public void evaluate(ChainTerm<?>.MathChain chain, ChainTerm<?>.MathChain.MathIterator iterator) {
			if (iterator.peek().getFirst() != NULL_TERM)
				throw new RuntimeException(
						"The null delete operator was applied to a non null-term term. This is a mistake on the developer's end.");
			iterator.peek().setFirst(iterator.current().getFirst());
			iterator.remove();
		}

		@Override
		public Term<?> evaluate(Term<?> first, Term<?> second) {
			throw new UnsupportedOperationException(
					"The null delete operator is not meant to actually evaluate anything.");
		}

	};

	public static final Term<Data<Void>> NULL_TERM = () -> {
		throw new RuntimeException("The Null Term was evaluated. :( This is a developer issue.");
	};

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
