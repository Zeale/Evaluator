package org.alixia.libs.evaluator.api.terms;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.alixia.libs.evaluator.api.Chain;
import org.alixia.libs.evaluator.api.operators.NormalOperator;
import org.alixia.libs.evaluator.api.operators.Operator;
import org.alixia.libs.evaluator.api.operators.Precedented;
import org.alixia.libs.evaluator.api.operators.Precedented.Precedence;
import org.alixia.libs.evaluator.api.types.Data;

public class ChainTerm<T extends Data<?>> implements Term<T> {

	public interface TriCombiner<I1, I2, I3, R> {
		R combine(I1 input1, I2 input2, I3 input3);
	}

	public final class MathChain extends Chain<Term<T>, NormalOperator> {

		private final Set<Precedence> precedences = new TreeSet<>(Collections.reverseOrder());

		private MathChain(final Term<T> first) {
			super(first);
		}

		@Override
		public boolean add(final NormalOperator second, final Term<T> first) {
			boolean result = super.add(second, first);
			if (result)
				if (second instanceof Precedented)
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
			 * Takes two <b>{@link Pair}</b>, the current one, and the previous one. The
			 * {@link Term}s from the first and second Pairs are used, and the
			 * {@link Operator} from the first Pair is used. The combiner (typically) uses
			 * the operator to combine the two terms, then returns a term. After this, the
			 * previous {@link Pair} is removed from the {@link Chain}, and the other
			 * {@link Pair} gets its {@link Term} value replaced with the result of the
			 * combiner.
			 * <p>
			 * The position of this iterator is decremented in this operation, so that <b>no
			 * change is noticed by subsequent calls to {@link #current()}</b>, and other
			 * methods in this iterator that don't move the position backwards.
			 * </p>
			 * <p>
			 * This method is also safe to use regardless of the position of the iterator,
			 * since combinations can occur validly between the last and second to last
			 * {@link Pair}s in the {@link Chain}, and an {@link IndexOutOfBoundsException}
			 * will be thrown if the position is greater than such which would undergo the
			 * previously aforementioned operation.
			 * 
			 * @param combiner The {@link TriCombiner} which combines the {@link Term}s
			 *                 given an operator.
			 */
			public void combineCurrentWithLast(
					TriCombiner<? super Term<T>, ? super NormalOperator, ? super Term<T>, ? extends NormalOperator> combiner) {
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
					((Operator) pair.getSecond()).evaluate(chain, iterator.position(),
							(ChainTerm<?>.MathChain.MathIterator) iterator);
			}

		// TODO Take care of non-precedented operators.
		for (final MathChain.MathIterator iterator = chain.iterator(); iterator.hasNext();) {
			final Chain<Term<T>, NormalOperator>.Pair pair = iterator.next();
			if (pair.isLast())
				break;
			((Operator) pair.getSecond()).evaluate(chain, iterator.position(),
					(ChainTerm<?>.MathChain.MathIterator) iterator);
		}

		if (!(chain.size() == 1))
			try {
				throw new RuntimeException("A ChainTerm was evaluated, but ended up having a size greater than one.");
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
