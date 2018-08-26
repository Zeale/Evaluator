package org.alixia.libs.evaluator.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Chain<F, S> implements List<Chain<F, S>.Pair> {

	private final LinkedList<Pair> items = new LinkedList<>();

	public final class Pair {

		private boolean last;

		public boolean isLast() {
			return last;
		}

		private F first;
		private S second;

		public F getFirst() {
			return first;
		}

		public S getSecond() {
			return second;
		}

		public void setFirst(F first) {
			this.first = first;
		}

		public void setSecond(S second) {
			this.second = second;
		}

		private Pair(F first, S second) {
			this.first = first;
			this.second = second;
		}

	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		// This should NEVER return true.
		return items.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof Chain.Pair)
			return items.contains(o);
		else
			for (Pair p : items)
				if (o == p.first || o == p.second)
					return true;
		return false;
	}

	@Override
	public Iterator<Chain<F, S>.Pair> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		return items.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return items.toArray(a);
	}

	public boolean add(S second, F first) {
		Pair previousLast = items.getLast();// Get the item that is currently last.
		previousLast.last = false;// It's not gonna be last after this operation.
		previousLast.second = second;// Assign its second piece a value.

		Pair newLast = new Pair(first, null);
		newLast.last = true;// Mark this as the last.
		items.add(newLast);// Add the new last to the array.
		return true;
	}

	/**
	 * Adds the specified {@link Pair} right before the last Pair in this
	 * {@link Chain}. If this Chain already contains the specified Pair, this Chain
	 * will create a new Pair with the same values as the specified one, and put
	 * that in the aforementioned space.
	 * 
	 * @return <code>true</code>, since this method doesn't explicitly deny
	 *         execution under any conditions.
	 */
	@Override
	public boolean add(Pair pair) {
		if (items.contains(pair))
			pair = new Pair(pair.first, pair.second);
		items.add(items.size() - 1, pair);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		return items.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return items.containsAll(c);
	}

	/**
	 * Removes all but the last element in this {@link Chain}.
	 */
	@Override
	public void clear() {
		Pair last = items.getLast();
		items.clear();
		items.add(last);
	}

	@Override
	public Chain<F, S>.Pair get(int index) {
		return items.get(index);
	}

	@Override
	public Chain<F, S>.Pair set(int index, Chain<F, S>.Pair element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(int index, Chain<F, S>.Pair element) {
		// TODO Auto-generated method stub

	}

	@Override
	public Chain<F, S>.Pair remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<Chain<F, S>.Pair> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Chain<F, S>.Pair> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Chain<F, S>.Pair> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
