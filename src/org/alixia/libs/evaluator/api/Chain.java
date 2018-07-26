package org.alixia.libs.evaluator.api;

public class Chain<F, S> {

	private First start;
	private First current;

	private class First {
		private F value;
		private Second next;

		public First(F value) {
			this.value = value;
		}
	}

	private class Second {
		private S value;
		private First next;

		public Second(S value) {
			this.value = value;
		}
	}

	public F getF(int position) {
		return getFirst(position).value;
	}

	private First getFirst(int position) {
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

	public S getS(int position) {
		Second current = getFirst(position).next;
		if (current == null)
			throw new IndexOutOfBoundsException();
		else
			return current.value;
	}

	public Chain(F first) {
		start = current = new First(first);
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
	public void replaceF(int pos, F f) {
		getFirst(pos).value = f;
	}

	public void replaceS(int pos, S s) {
		Second current = getFirst(pos).next;
		if (current == null)
			throw new IndexOutOfBoundsException();
		current.value = s;
	}

	/**
	 * Removes the F and S at the given position in the chain.
	 * 
	 * @param pos
	 *            The position of the F and S that will be removed.
	 */
	public void remove(int pos) {
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

	public void append(S second, F first) {
		Second nextS = new Second(second);
		current.next = nextS;
		current = nextS.next = new First(first);
	}

	@Override
	public String toString() {
		First f = start;
		if (f == null)
			return "[]";
		String s = super.toString() + "[[" + f.value;
		while (f.next != null)
			s += ", " + f.next.value + "], [" + (f = f.next.next).value + "]";
		return s + "]";
	}

}
