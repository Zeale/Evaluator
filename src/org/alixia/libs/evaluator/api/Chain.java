package org.alixia.libs.evaluator.api;

import java.util.Iterator;

public class Chain<F, S> implements Iterable<Chain<F, S>.Pair> {

	public final class ChainIterator implements Iterator<Pair> {

		private First current, previous;

		private boolean walked;

		public void combine(final Combiner<F, F, S, F> combiner) {
			if (current.next == null)
				throw new IndexOutOfBoundsException("Can't combine just the last term of a chain.");
			current.next.next.value = combiner.combine(current.value, current.next.value, current.next.next.value);
			remove();
		}

		@Override
		public boolean hasNext() {
			return current == null || current.next != null;
		}

		@Override
		public Pair next() {
			assert previous == null || previous.next.next == current;
			walk();
			return new Pair(current);
		}

		@Override
		public void remove() {
			walked = true;
			if (previous == null)
				start = current = current.next.next;
			else {
				previous.next.next = current.next.next;
				current = current.next.next;
			}
		}

		private void walk() {
			if (walked) {
				walked = false;
				return;
			}
			previous = current;
			current = current == null ? start : current.next.next;
		}

	}

	public interface Combiner<R, F, S, T> {
		R combine(F f, S s, T t);
	}

	private class First {
		private F value;
		private Second next;

		public First(final F value) {
			this.value = value;
		}
	}

	public final class Pair {

		private final First first;

		private Pair(final First first) {
			this.first = first;
		}

		public Box<F> getFirst() {
			return new Box<>(first.value);
		}

		public Box<F> getNext() {
			return first.next == null ? null : new Box<>(first.next.next.value);
		}

		public Box<S> getSecond() {
			return first.next == null ? null : new Box<>(first.next.value);
		}

		@Override
		public String toString() {
			return "{" + getFirst().item + ", " + getSecond().item + "}";
		}

	}

	private class Second {
		private S value;
		private First next;

		public Second(final S value) {
			this.value = value;
		}
	}

	private First start;

	private First current;

	public Chain(final F first) {
		start = current = new First(first);
	}

	public void append(final S second, final F first) {
		final Second nextS = new Second(second);
		current.next = nextS;
		current = nextS.next = new First(first);
	}

	public F getF(final int position) {
		return getFirst(position).value;
	}

	private First getFirst(final int position) {
		if (position < 0)
			throw new IndexOutOfBoundsException();
		First current = start;
		for (int i = 0; i < position; i++)
			if (current.next.next == null)
				throw new IndexOutOfBoundsException();
			else
				current = current.next.next;
		return current;
	}

	public F getFront() {
		return start.value;
	}

	public S getS(final int position) {
		final Second current = getFirst(position).next;
		if (current == null)
			throw new IndexOutOfBoundsException();
		else
			return current.value;
	}

	@Override
	public ChainIterator iterator() {
		return new ChainIterator();
	}

	public boolean linked() {
		return start.next != null;
	}

	/**
	 * Removes the F and S at the given position in the chain.
	 *
	 * @param pos
	 *            The position of the F and S that will be removed.
	 */
	public void remove(final int pos) {
		First prev, next = start;
		if (pos < 0)
			throw new IndexOutOfBoundsException();
		prev = pos == 0 ? null : getFirst(pos - 1);
		for (int i = 0; i <= pos; i++)
			if (next.next == null)
				if (i == pos)
					next = null;
				else
					throw new IndexOutOfBoundsException();
			else
				next = next.next.next;
		if (next == null)
			prev.next.next = null;
		else if (prev == null)
			start = start.next.next;
		else
			prev.next.next = next;
	}

	/**
	 * Replaces the F at the specified position with the given F.
	 *
	 * @param pos
	 *            The <code>pos+1</code>th F in the chain will be replaced with the
	 *            specified F.
	 * @param f
	 *            The new value.
	 */
	public void replaceF(final int pos, final F f) {
		getFirst(pos).value = f;
	}

	public void replaceS(final int pos, final S s) {
		final Second current = getFirst(pos).next;
		if (current == null)
			throw new IndexOutOfBoundsException();
		current.value = s;
	}

	@Override
	public String toString() {
		First f = start;
		if (f == null)
			return "[]";
		String s = "Chain[[" + f.value;
		while (f.next != null)
			s += ", " + f.next.value + "], [" + (f = f.next.next).value + "]";
		return s + "]";
	}

}
