package org.alixia.libs.evaluator;

import org.alixia.libs.evaluator.api.Spate;
import org.alixia.libs.evaluator.api.operators.NormalOperator;
import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.wrappers.StandardWrapper;

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
	public synchronized ChainTerm<?> chain(Spate<Character> equation) {
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
		return parse;
	}

	public synchronized double solve(Spate<Character> equation) {
		return (double) chain(equation).evaluate();
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
			else if (c == '(') {
				ChainTerm<?> nest = parseNest(StandardWrapper.PARENTHESES);
				if (nest == null)
					throw new RuntimeException("Error while parsing some parentheses' content.");
				return nest;
			} else if (Character.isDigit(c) || c == '.') {
				String numb = "";
				boolean encounteredDecimal = false;
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
			} else if (c == -1)
				throw new RuntimeException("Expected a term but found the end of the equation.");
			else if (!Character.isWhitespace(c))
				throw new RuntimeException("Unexpected character while parsing a term: " + (char) c);
			equation.skip();
		} // Leaves off before first digit.

	}

	private ChainTerm<?> parseNest(StandardWrapper parentheses) {

		// Should be called when peek() returns an opening parenthesis.
		int meets = 0;
		String chain = "";
		int c;

		// SKIPS ANY CHARACTERS BEFORE MEETING ITS OPENING PARENTHESIS
		while (true) {
			c = box(equation.next());
			if (c == -1)
				return null;
			else if (c == parentheses.getOpenner())
				break;
		}
		meets++;
		while (true) {
			c = box(equation.peek());
			if (c == -1)
				throw new RuntimeException("Equation ends prematurely; a closing '" + parentheses.getCloser()
						+ "' was expected but was not found.");
			else if (c == parentheses.getOpenner())
				meets++;
			else if (c == parentheses.getCloser())
				meets--;
			equation.skip();
			if (meets == 0)
				break;
			chain += (char) c;
		}
		System.out.println(chain);
		return new Evaluator<Double>().chain(Spate.spate(chain));
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
		else if (c == '^')
			return EXPONENTIATION;
		else
			throw new RuntimeException("Could not parse the operator, '" + (char) c + "'");
	}

	private void clearWhitespace(String err) {
		Character c;
		while (Character.isWhitespace(box(c = equation.peek())))
			equation.skip();
		if (err != null && c == null)
			throw new RuntimeException(err);
	}

}
