package org.alixia.libs.evaluator;

import org.alixia.libs.evaluator.api.Spate;

public class Evaluator<T extends Number> {

	private Evaluator() {
	}

	private Spate<Character> equation;

	public synchronized double solve(Spate<Character> equation) {
		this.equation = equation;
		// TODO Check pre-solved value if it exists (or maybe don't; Spate may have
		// changed).
		while (equation.hasNext())
			;
		return 0;
	}

}
