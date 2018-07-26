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


	public Chain(F first) {
		start = current = new First(first);
	}

	public void append(S second, F first) {
		Second nextS = new Second(second);
		current.next = nextS;
		current = nextS.next = new First(first);
	}

}
