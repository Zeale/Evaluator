package org.alixia.libs.evaluator;

import org.alixia.libs.evaluator.api.NormalOperator;
import org.alixia.libs.evaluator.api.Spate;
import org.alixia.libs.evaluator.api.Term;

public class Evaluator<T extends Number> {

	private Evaluator() {
	}

	private Spate<Character> equation;

	public static void main(String[] args) {
		System.out.println(new Evaluator<>().solve(Spate.spate("14.385")));
	}

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
			c = box(equation.peek());
			if (c == '+')
				negate = false;
			else if (c == '-')
				negate ^= true;
			else if (Character.isWhitespace(c))
				;
			else if (Character.isDigit(c) || c == '.')
				break;// Break before moving Spate's position to this digit we've found.
			else if (c == -1)
				throw new RuntimeException("Expected a term but found the end of the equation.");
			else
				throw new RuntimeException("Unexpected character while parsing a term: " + (char) c);
			equation.skip();
		} // Leaves off before first digit.

		String numb = "";
		boolean encounteredDecimal = false;

		// TODO Parse number.
		while (true) {
			c = box(equation.peek());
			if (c == '.') {
				if (encounteredDecimal)
					throw new RuntimeException("Encountered multiple decimal points in a number.");
				encounteredDecimal = true;
				numb += (char) c;
			} else if (Character.isDigit(c))
				numb += (char) c;
			else// If an unexpected char is found, assume end of term. This may be changed
				// later, but, until then, with the addition of operators later on, this
				// behavior will remain safe.
				return new org.alixia.libs.evaluator.api.Number<Double>((negate ? -1 : 1) * Double.parseDouble(numb));
			equation.skip();// We only go on to the next char
			// (and move the spate's position over by one) if we are not done parsing this
			// term. This way, this method complete's with the spate's position right before
			// the next operator's first char. We need to finish one char before the next
			// thing we need to parse, bc I'm an idiot and I didn't add a "curr()" method to
			// the Spate class (I didn't want to force some Spates that are built on top of
			// other APIs to have to cache the current character they are on).
		}
	}

	private NormalOperator<?, ?, ?> parseOperator() {
		// TODO Implement
		return null;
	}

}
