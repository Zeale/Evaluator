package org.alixia.libs.evaluator.api.terms;

import static org.alixia.libs.evaluator.api.operators.StandardOperators.DIVIDE;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.EXPONENTIATION;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.MODULUS;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.MULTIPLY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alixia.libs.evaluator.api.Box;
import org.alixia.libs.evaluator.api.Chain;
import org.alixia.libs.evaluator.api.Chain.Combiner;
import org.alixia.libs.evaluator.api.operators.NormalOperator;

public class ChainTerm<T> implements Term<T> {

	private final Chain<Term<T>, NormalOperator<T, T, T>> chain;

	public ChainTerm(Term<T> first) {
		if (first == null)
			throw null;
		chain = new Chain<Term<T>, NormalOperator<T, T, T>>(first);
	}

	public void append(NormalOperator<T, T, T> operator, Term<T> term) {
		if (operator == null || term == null)
			throw null;
		chain.append(operator, term);
	}

	@Override
	public String toString() {
		return evaluate().toString();
	}

	@Override
	public T evaluate() {
		// TODO Get more efficient algorithm
		// TODO Also, give (some) operators a precedence that will determine when they
		// are evaluated.
		Term<T> value = chain.getFront();
		Combiner<Term<T>, Term<T>, NormalOperator<T, T, T>, Term<T>> combiner = (f, s, t) -> s.evaluate(f, t);
		for (Chain<Term<T>, NormalOperator<T, T, T>>.ChainIterator iterator = chain.iterator(); iterator.hasNext();) {
			Chain<Term<T>, NormalOperator<T, T, T>>.Pair pair = iterator.next();
			if (pair.getSecond() == null)// Signifies that getFirst has returned the last Term<T> in the Chain.
				break;
			if (pair.getSecond().item == EXPONENTIATION)
				iterator.combine(combiner);
		}
		for (Chain<Term<T>, NormalOperator<T, T, T>>.ChainIterator iterator = chain.iterator(); iterator.hasNext();) {
			Box<NormalOperator<T, T, T>> operatorBox = iterator.next().getSecond();
			if (operatorBox == null)
				break;
			NormalOperator<T, T, T> operator = operatorBox.item;
			if (operator == DIVIDE || operator == MULTIPLY || operator == MODULUS)
				iterator.combine(combiner);
		}
		while (chain.linked()) {
			value = chain.getS(0).evaluate(value, chain.getF(1));
			chain.replaceF(1, value);
			chain.remove(0);
		}
		return value.evaluate();
	}

}
