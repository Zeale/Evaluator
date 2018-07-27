package org.alixia.libs.evaluator.api.operators;

public interface Precedented {
	public final class Precedence implements Comparable<Precedence> {
		private final int precedence;

		public Precedence(int precedence) {
			this.precedence = precedence;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + precedence;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Precedented))
				return false;
			Precedence other = (Precedence) obj;
			if (precedence != other.precedence)
				return false;
			return true;
		}

		@Override
		public int compareTo(Precedence o) {
			return ((Integer) precedence).compareTo(o.precedence);
		}

		@Override
		public String toString() {
			return "" + precedence;
		}

	}

	Precedence precedence();
}
