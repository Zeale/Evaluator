package org.alixia.libs.evaluator.api.terms;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.alixia.libs.evaluator.api.Chain;
import org.alixia.libs.evaluator.api.exceptions.DeveloperException;
import org.alixia.libs.evaluator.api.operators.NormalOperator;
import org.alixia.libs.evaluator.api.operators.Precedented;
import org.alixia.libs.evaluator.api.operators.Precedented.Precedence;
import org.alixia.libs.evaluator.api.types.Data;

public class ChainTerm<T extends Data<?>> implements Term<T> {

	public final class MathChain extends Chain<Term<T>, NormalOperator> {

		private final Set<Precedence> precedences = new TreeSet<>(Collections.reverseOrder());

		private MathChain(final Term<T> first) {
			super(first);
		}

		@Override
		public boolean add(final NormalOperator second, final Term<T> first) {
			boolean result = super.add(second, first);
			if (result)
				if (second instanceof Precedented && !((Precedented) second).precedence().equals(Precedence.NONE))
					precedences.add(((Precedented) second).precedence());
			return result;
		}

		private Set<Precedence> getPrecedences() {
			return Collections.unmodifiableSet(precedences);
		}

		@Override
		public MathIterator iterator() {
			return new MathIterator();
		}

		public class MathIterator extends ChainIterator {

			/**
			 * <p>
			 * This operation works if the previous {@link Pair}'s second item is a
			 * {@link NormalOperator}. Otherwise, it does nothing (apart from throw an
			 * exception if the position of this iterator is <code>0</code>, and this
			 * iterator can't get a hold of an operator, since there is no {@link Pair}
			 * containing one behind the current {@link Pair}).
			 * </p>
			 * <p>
			 * Takes two <b>{@link Pair}</b>, the current one, and the previous one. The
			 * {@link Term}s from the first and second Pairs are used, and the
			 * {@link Operator} from the first Pair is used. The combiner (typically) uses
			 * the operator to combine the two terms, then returns a term. After this, the
			 * previous {@link Pair} is removed from the {@link Chain}, and the other
			 * {@link Pair} gets its {@link Term} value replaced with the result of the
			 * combination.
			 * </p>
			 * <p>
			 * The position of this iterator is decremented in this operation, so that <b>no
			 * change is noticed by subsequent calls to {@link #current()}</b>, and other
			 * methods in this iterator that don't move the position backwards.
			 * </p>
			 * <p>
			 * This method is also safe to use at any position, apart from <code>0</code>.
			 * An {@link IndexOutOfBoundsException} will be thrown if the position is out of
			 * bounds.
			 * </p>
			 */
			@SuppressWarnings("unchecked")
			public void combineCurrentWithLast() {
				Pair previous = previous(), current = current();
				if (previous.getSecond() instanceof NormalOperator) {
					current.setFirst((Term<T>) ((NormalOperator) previous.getSecond()).evaluate(previous.getFirst(),
							current.getFirst()));
					removePrevious();
					skipBack();
				}
			}

			@SuppressWarnings("unchecked")
			public void combineCurrentAndPreviousWithNormalOp(NormalOperator operator) {
				Pair previous = previous(), current = current();
				current.setFirst((Term<T>) operator.evaluate(previous.getFirst(), current.getFirst()));
				removePrevious();
				skipBack();
			}

			@SuppressWarnings("unchecked")
			public void combineCurrentAndNextWithNormalOp(NormalOperator operator) {
				Pair current = current(), next = peek();
				next.setFirst((Term<T>) operator.evaluate(current.getFirst(), next.getFirst()));
				remove();
			}

		}

	}

	private final MathChain chain;

	public ChainTerm(final Term<T> first) {
		if (first == null)
			throw null;
		chain = new MathChain(first);
	}

	public void append(final NormalOperator operator, final Term<T> term) {
		if (operator == null || term == null)
			throw null;
		chain.add(operator, term);
	}

	@Override
	public T evaluate() {

		for (final Precedence i : chain.getPrecedences())
			for (final MathChain.MathIterator iterator = chain.iterator(); iterator.hasNext();) {
				final Chain<Term<T>, NormalOperator>.Pair pair = iterator.next();
				if (pair.isLast())// Signifies that we are at the last Pair in the Chain.
					break;
				if (pair.getSecond() instanceof Precedented && ((Precedented) pair.getSecond()).precedence().equals(i))
					iterator.combineCurrentAndNextWithNormalOp(pair.getSecond());
			}
		// TODO Take care of non-precedented operators.
		for (final MathChain.MathIterator iterator = chain.iterator(); iterator.hasNext();) {
			final Chain<Term<T>, NormalOperator>.Pair pair = iterator.next();
			if (pair.isLast())
				break;
			iterator.combineCurrentAndNextWithNormalOp(pair.getSecond());
		}

		if (chain.size() != 1)
			try {
				// This code might be completely useless; I haven't really fully thought about
				// how evaluation works, so there might not be a way for it to possibly mess up.
				throw new DeveloperException(
						"A ChainTerm was evaluated, but ended up having a size greater than one. You should totes report this to the developer, and tell them what equation caused this problem.");
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

		return chain.get(0).getFirst().evaluate();
	}

	@Override
	public String toString() {
		return evaluate().toString();
	}

}
