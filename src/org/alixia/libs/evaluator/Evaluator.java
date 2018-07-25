package org.alixia.libs.evaluator;

import org.alixia.libs.evaluator.api.NormalOperator;
import org.alixia.libs.evaluator.api.Spate;
import org.alixia.libs.evaluator.api.Term;

public class Evaluator<T extends Number> {

	private Evaluator() {
	}

	private Spate<Character> equation;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized double solve(Spate<Character> equation) {
		// Set the field so that other methods can use it.
		this.equation = equation;

		// Check to see if the equation is empty.
		Character c;
		while (Character.isWhitespace(box(c = equation.peek())))
			equation.skip();
		if (c == null)
			throw new RuntimeException("Equation has no evaluatable content.");

		Term<?> term = parseTerm();
		while (equation.hasNext())
			term = ((NormalOperator) parseOperator()).evaluate(term, parseTerm());
		return (double) term.evaluate();
	}

	private int box(Character character) {
		return character == null ? -1 : character;
	}

	private Term<?> parseTerm() {
		String numb = "";
		int c;

		// TODO Implement
		return null;
	}

	private NormalOperator<?, ?, ?> parseOperator() {
		// TODO Implement
		return null;
	}

}
