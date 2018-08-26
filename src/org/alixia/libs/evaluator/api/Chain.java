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

	public class ChainIterator implements Iterator<Pair> {

		private int position = -1;

		/**
		 * @return The size of the {@link Chain}.
		 */
		public int size() {
			return Chain.this.size();
		}

		/**
		 * Removes the item at the current position (so what {@link #current()} will
		 * return, and what {@link #next()} just returned). <b>Calling
		 * {@link #current()} after this will return something different from previous
		 * calls!</b>
		 * 
		 * @return The removed {@link Pair} object, if any. (This method returns
		 *         <code>null</code> if the {@link Chain}'s {@link Chain#remove(int)}
		 *         method returns <code>null</code>, which it does if the current item
		 *         is the last item. The last item in a {@link Chain} can't be removed.
		 *         I can be modified though.)
		 */
		public Pair removeAndGet() {
			Pair item = get(position);
			Chain.this.remove(position);
			return item;
		}

		@Override
		public void remove() {
			removeAndGet();
		}

		/**
		 * Returns how many times <code>next()</code> can be called.
		 * 
		 * @return <code>size() - position - 1</code>.
		 */
		public int remaining() {
			return size() - position - 1;
		}

		/**
		 * @return This iterator's current position.
		 */
		public int position() {
			return position;
		}

		/**
		 * Moves this iterator's position so that <code>peek()</code> will return the
		 * item <b>after</b> the specified <code>position</code>.
		 * 
		 * @param position Where to move to.
		 */
		public void moveTo(int position) {
			this.position = position;
		}

		/**
		 * The item at the previous position.
		 * 
		 * @return <code>get(position - 1)</code>
		 */
		public Pair previous() {
			return get(position - 1);
		}

		/**
		 * Returns the {@link Pair} at the current position. (In other words, returns
		 * what {@link #next()} just returned).
		 * 
		 * @return The {@link Pair} at the current position.
		 */
		public Pair current() {
			return get(position);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return position + 1 < size();
		}

		/**
		 * Skips over the current element. Executing
		 * 
		 * <pre>
		 * <code>
		 * skip()
		 * current()
		 * </code>
		 * </pre>
		 * 
		 * is equivalent to calling <code>next()</code>.
		 */
		public void skip() {
			position++;
		}

		/**
		 * Returns the next item with<b>out</b> moving forward.
		 * 
		 * @return <code>get(position+1)</code>
		 */
		public Pair peek() {
			return get(position + 1);
		}

		/**
		 * <p>
		 * Moves forward, then returns the item at the new position.
		 * </p>
		 * <p>
		 * <p>
		 * As long as the position of this iterator is not changed (via any other method
		 * or this method), and the underlying {@link Chain} is not modified, calling
		 * {@link #current()} will always return the same value as the previous call to
		 * {@link #next()}.
		 * </p>
		 * If the iterator is at 2:
		 * 
		 * <pre>
		 * <code>0 1 2 3 4</code>
		 * <code>    ^</code>
		 * </pre>
		 * 
		 * then next will move to <code>3</code> and return <code>3</code>.
		 * </p>
		 * 
		 * @return <code>get(++position)</code>
		 */
		@Override
		public Pair next() {
			return get(++position);
		}

	}

	/**
	 * No check or error is done or thrown by modifications occurring outside this
	 * iterator, while this iterator is in use.
	 * 
	 * @return A new {@link ChainIterator} over this {@link Chain}.
	 */
	public ChainIterator iterator() {
		return new ChainIterator();
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
