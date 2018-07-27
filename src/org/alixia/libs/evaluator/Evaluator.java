package org.alixia.libs.evaluator;

import org.alixia.libs.evaluator.api.Spate;
import org.alixia.libs.evaluator.api.operators.NormalOperator;
import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.*;

import java.util.Scanner;

public class Evaluator<T extends Number> {

	private Evaluator() {
	}

	private Spate<Character> equation;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine())
			System.out.println(new Evaluator<>().solve(Spate.spate(scanner.nextLine())));
		scanner.close();
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

		ChainTerm<?> parse = new ChainTerm<>(parseTerm());
		clearWhitespace(null);
		while (equation.hasNext()) {
			parse.append((NormalOperator) parseOperator(), (Term) parseTerm());
			clearWhitespace(null);// For the while loop.
		}
		return (double) parse.evaluate();
	}

	private int box(Character character) {
		return character == null ? -1 : character;
	}

	private Term<?> parseTerm() {

		// Check for whitespace. Stop where equation.next() will return the first
		// non-whitepsace char.
		clearWhitespace("The equation ended prematurely; another term was expected.");

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
			else {// If an unexpected char is found, assume end of term. This may be changed
					// later, but, until then, with the addition of operators later on, this
					// behavior will remain safe.
				if (numb.charAt(numb.length() - 1) == '.')
					throw new RuntimeException("Unnecessary decimal found.");
				return new org.alixia.libs.evaluator.api.terms.Number<Double>(
						(negate ? -1 : 1) * Double.parseDouble(numb));
			}
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
		clearWhitespace("The equation ended permaturely; an operator was expected.");
		int c = equation.next();
		if (c == '+')
			return ADD;
		else if (c == '-')
			return SUBTRACT;
		else if (c == '*')
			return MULTIPLY;
		else if (c == '/' || c == '\u00f7')
			return DIVIDE;
		else if (c == '%')
			return MODULUS;

		return null;
	}

	private void clearWhitespace(String err) {
		Character c;
		while (Character.isWhitespace(box(c = equation.peek())))
			equation.skip();
		if (err != null && c == null)
			throw new RuntimeException(err);
	}

}
