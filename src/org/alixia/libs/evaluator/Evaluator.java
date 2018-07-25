package org.alixia.libs.evaluator;

import org.alixia.libs.evaluator.api.NormalOperator;
import org.alixia.libs.evaluator.api.Spate;
import org.alixia.libs.evaluator.api.Term;

public class Evaluator<T extends Number> {

	private Evaluator() {
	}

	private Spate<Character> equation;

	public synchronized double solve(Spate<Character> equation) {
		// Set the field so that other methods can use it.
		this.equation = equation;

		// Check to see if the equation is empty.
		Character c;
		while (true) {

		}

		Term<?> term = parseTerm();
		while (equation.hasNext())
			parseTerm();//
		;
		return 0;
	}

	private Term<?> parseTerm() {

	}

	private NormalOperator<?, ?, ?> parseOperator() {

	}

}
