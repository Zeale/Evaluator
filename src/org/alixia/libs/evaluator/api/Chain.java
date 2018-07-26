package org.alixia.libs.evaluator.api;

public class Chain<F, S> {

	private final First start;
	private First current;

	private class First {
		private final F value;
		private Second next;

		public First(F value) {
			this.value = value;
		}
	}

	private class Second {
		private final S value;
		private First next;

		public Second(S value) {
			this.value = value;
		}
	}

	public F getF(int position) {
		return getFirst(position).value;
	}

	private First getFirst(int position) {
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

	public void append(S second, F first) {
		Second nextS = new Second(second);
		current.next = nextS;
		current = nextS.next = new First(first);
	}

}
