package org.alixia.libs.evaluator.api.operators;

public interface Precedented {
	public final class Precedence implements Comparable<Precedence> {
		private final int precedence;

		public Precedence(final int precedence) {
			this.precedence = precedence;
		}

		@Override
		public int compareTo(final Precedence o) {
			return ((Integer) precedence).compareTo(o.precedence);
		}

		@Override
		public boolean equals(final Object obj) {
			return obj != null && obj instanceof Precedence && ((Precedence) obj).precedence == precedence;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + precedence;
			return result;
		}

		@Override
		public String toString() {
			return "" + precedence;
		}
		
		public static final Precedence NONE = new Precedence(Integer.MIN_VALUE);

	}

	Precedence precedence();
}
