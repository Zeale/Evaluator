package org.alixia.libs.evaluator;

import org.alixia.libs.evaluator.api.Spate;
import org.alixia.libs.evaluator.api.functions.SimpleFunction;
import org.alixia.libs.evaluator.api.operators.NormalOperator;
import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.wrappers.StandardWrapper;
import org.alixia.libs.evaluator.api.terms.Number;

import static org.alixia.libs.evaluator.api.operators.StandardOperators.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Evaluator<T extends java.lang.Number> {

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

	@SuppressWarnings("unchecked")
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
			} else if (Character.isLetter(c) || c == '_') {
				String name = "" + (char) c;
				equation.skip();// equation is now positioned at first function name char
				while ((c = equation.peek()) == '_' || Character.isLetterOrDigit(c)) {
					name += (char) c;
					equation.skip();
				}

				// TODO Match name against name database to determine if it's a function or
				// variable.

				// Functions have a higher precedence than vars; if there is a name conflict, a
				// the name will be parsed as the function rather than a variable, unless the
				// "function::functionName" specifying syntax is used, or a tilde is used to
				// force variable treatment.
				if (c == '[' || c == '(') {
					@SuppressWarnings("rawtypes")
					SimpleFunction function = SimpleFunction.getFunction(name);
					List<String> args = parseFunctionArgs(StandardWrapper.openValueOf((char) c));
					if (function == null)
						throw new RuntimeException("Invalid function name: " + name
								+ "; couldn't find a function with the specified name.");
					else if (args.isEmpty())
						throw new RuntimeException("Not enough arguments given for the function: " + name + ".");
					else if (args.size() > 1)
						throw new RuntimeException("Excessive arguments passed to function, " + name + ".");
					return function.evaluate(new Evaluator<>().chain(Spate.spate(args.get(0))).evaluate());
				}

				// TODO Parse function
				else
					throw new RuntimeException("Variables are not yet supported.");

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

	private List<String> parseFunctionArgs(StandardWrapper parentheses) {
		// When called, peek() should return an opening parenthesis.

		int meets = 0;
		int c;

		while (true) {
			c = box(equation.next());
			if (c == -1)
				return null;
			else if (c == parentheses.getOpenner())
				break;
		}
		meets++;

		List<String> args = new LinkedList<>();
		String arg = "";
		while (true) {
			c = box(equation.peek());
			if (c == -1)
				throw new RuntimeException("Equation ends prematurely; a closing '" + parentheses.getCloser()
						+ "' was expected for a function, but was not found.");
			else if (c == parentheses.getOpenner())
				meets++;
			else if (c == parentheses.getCloser())
				meets--;
			equation.skip();
			if (meets == 0)
				break;
			else if (meets == 1 && c == ',') {
				args.add(arg.trim());
				arg = "";
				continue;
			}
			arg += (char) c;
		}

		return args;
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
						+ "' was expected, but was not found.");
			else if (c == parentheses.getOpenner())
				meets++;
			else if (c == parentheses.getCloser())
				meets--;
			equation.skip();
			if (meets == 0)
				break;
			chain += (char) c;
		}
		return new Evaluator<Double>().chain(Spate.spate(chain));
	}

	private NormalOperator<?, ?, ?> parseOperator() {
		clearWhitespace("The equation ended permaturely; an operator was expected.");

		int c = equation.peek();
		if (c == '(' || Character.isLetterOrDigit(c) || c == '.' || c == '_')
			return MULTIPLY;

		equation.skip();
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
