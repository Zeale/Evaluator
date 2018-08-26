package org.alixia.libs.evaluator.api;

public class Chain<F, S> {

	public final class Pair {

		private boolean last;

		public boolean isLast() {
			return last;
		}

		private final F first;
		private final S second;

		public F getFirst() {
			return first;
		}

		public S getSecond() {
			return second;
		}

		private Pair(F first, S second) {
			this.first = first;
			this.second = second;
		}

	}

}
