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

		// Check for whitespace.
		{
			Character c;
			while (Character.isWhitespace(box(c = equation.peek())))
				equation.skip();
			if (c == null)
				throw new RuntimeException("The equation ended prematurely; another term was expected.");
		}

		int c;
		boolean negate = false;

		while (true) {
			c = box(equation.next());
			if (c == '+')
				negate = false;
			else if (c == '-')
				negate ^= true;
			else if (Character.isWhitespace(c))
				continue;
			else if (Character.isDigit(c))
				break;
			else if (c == -1)
				throw new RuntimeException("Expected a term but found the end of the equation.");
			else
				throw new RuntimeException("Unexpected character while parsing a term: " + (char) c);
		}

		String numb = "";

		// TODO Implement
		return null;
	}

	private NormalOperator<?, ?, ?> parseOperator() {
		// TODO Implement
		return null;
	}

}
