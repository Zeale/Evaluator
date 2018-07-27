package org.alixia.libs.evaluator.api.terms;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.alixia.libs.evaluator.api.Chain;
import org.alixia.libs.evaluator.api.Chain.Combiner;
import org.alixia.libs.evaluator.api.operators.NormalOperator;
import org.alixia.libs.evaluator.api.operators.Precedented;
import org.alixia.libs.evaluator.api.operators.Precedented.Precedence;

public class ChainTerm<T> implements Term<T> {

	private final class MathChain extends Chain<Term<T>, NormalOperator<T, T, T>> {

		private final Set<Precedence> precedences = new TreeSet<>(Collections.reverseOrder());

		public Set<Precedence> getPrecedences() {
			return Collections.unmodifiableSet(precedences);
		}

		public MathChain(Term<T> first) {
			super(first);
		}

		@Override
		public void append(NormalOperator<T, T, T> second, Term<T> first) {
			super.append(second, first);
			if (second instanceof Precedented)
				precedences.add(((Precedented) second).precedence());
		}

	}

	private final MathChain chain;

	public ChainTerm(Term<T> first) {
		if (first == null)
			throw null;
		chain = new MathChain(first);
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
		Combiner<Term<T>, Term<T>, NormalOperator<T, T, T>, Term<T>> combiner = (f, s, t) -> s.evaluate(f, t);

		for (Precedence i : chain.getPrecedences())
			for (Chain<Term<T>, NormalOperator<T, T, T>>.ChainIterator iterator = chain.iterator(); iterator
					.hasNext();) {
				Chain<Term<T>, NormalOperator<T, T, T>>.Pair pair = iterator.next();
				if (pair.getSecond() == null)// Signifies that getFirst has returned the last Term<T> in the Chain.
					break;

				if (pair.getSecond().item instanceof Precedented
						&& ((Precedented) pair.getSecond().item).precedence().equals(i))
					iterator.combine(combiner);
				// System.out.println("COMBINED; NEW CHAIN: " + chain);
			}
		// System.out.println("\n");

		System.out.println(chain);
		Term<T> value = chain.getFront();
		// Take care of non-precedented operators.
		while (chain.linked()) {
			value = chain.getS(0).evaluate(value, chain.getF(1));
			chain.replaceF(1, value);
			chain.remove(0);
		}
		return value.evaluate();
	}

}
