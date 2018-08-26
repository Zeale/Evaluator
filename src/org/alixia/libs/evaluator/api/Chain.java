package org.alixia.libs.evaluator.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Chain<F, S> {

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

	public int size() {
		return items.size();
	}

	public boolean isEmpty() {
		// This should NEVER return true.
		return items.isEmpty();
	}

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

	public Object[] toArray() {
		return items.toArray();
	}

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
	public boolean add(Pair pair) {
		if (items.contains(pair))
			pair = new Pair(pair.first, pair.second);
		items.add(items.size() - 1, pair);
		return true;
	}

	public boolean remove(Object o) {
		return items.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return items.containsAll(c);
	}

	/**
	 * Removes all but the last element in this {@link Chain}.
	 */
	public void clear() {
		Pair last = items.getLast();
		items.clear();
		items.add(last);
	}

	public Chain<F, S>.Pair get(int index) {
		return items.get(index);
	}

	public Chain<F, S>.Pair set(int index, Chain<F, S>.Pair element) {
		return items.set(index, element);
	}

	public F setFirst(int pairIndex, F element) {
		Pair pair = get(pairIndex);// The pair itself is not moved, its "first" variable is modified.
		F old = pair.getFirst();
		pair.setFirst(element);
		return old;
	}

	public S setSecond(int pairIndex, S element) {
		Pair pair = get(pairIndex);// Only the "second" var is modified. See setFirst(...).
		S old = pair.getSecond();
		pair.setSecond(element);
		return old;
	}

	public static void main(String[] args) {
		List<String> list = new LinkedList<>();
		list.add("Potato");
		System.out.println(list);
	}

	public void add(int index, Chain<F, S>.Pair element) {
		if (index == size())
			add(element);
		else
			items.add(index, element);
	}

	public Chain<F, S>.Pair remove(int index) {
		return index != size() ? items.remove(index) : null;
	}

	public int indexOf(Object o) {
		if (o instanceof Chain.Pair)
			return items.indexOf(o);
		else
			for (int i = 0; i < size(); i++) {
				Pair pair = items.get(i);
				if (o == pair.first || o == pair.second)
					return i;
			}
		return -1;
	}

	public int lastIndexOf(Object o) {
		if (o instanceof Chain.Pair)
			return items.lastIndexOf(o);
		else
			for (int i = size() - 1; i >= 0; i--) {
				Pair pair = items.get(i);
				if (o == pair.first || o == pair.second)
					return i;
			}
		return -1;
	}

	public boolean addAll(Collection<? extends Chain<F, S>.Pair> c) {
		boolean result = false;
		for (Pair p : c)
			if (add(p))
				result = true;
		return result;
	}

}
