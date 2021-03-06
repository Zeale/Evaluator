package org.alixia.libs.evaluator;

import static org.alixia.libs.evaluator.api.operators.StandardOperators.ADD;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.DIVIDE;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.EXPONENTIATION;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.MODULUS;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.MULTIPLY;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.SUBTRACT;
import static org.alixia.libs.evaluator.api.operators.StandardOperators.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.alixia.libs.evaluator.api.Spate;
import org.alixia.libs.evaluator.api.VariableMap;
import org.alixia.libs.evaluator.api.VariableMap.Variable;
import org.alixia.libs.evaluator.api.functions.SimpleFunction;
import org.alixia.libs.evaluator.api.operators.NormalOperator;
import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.BooleanData;
import org.alixia.libs.evaluator.api.types.Data;
import org.alixia.libs.evaluator.api.types.NumericData;
import org.alixia.libs.evaluator.api.types.ProbabilityData;
import org.alixia.libs.evaluator.api.types.StringData;
import org.alixia.libs.evaluator.api.types.TimeData;
import org.alixia.libs.evaluator.api.types.casting.SimpleTypeMap;
import org.alixia.libs.evaluator.api.wrappers.StandardWrapper;

public class Evaluator {

	public static final int MAXIMUM_BIG_DECIMAL_DIVISION_SCALE = 4679;

	private final VariableMap variableMap = new VariableMap();
	private final SimpleTypeMap typeMap = new SimpleTypeMap();
	{
		// TODO Add BooleanData cast.
		typeMap.new Type(NumericData.class, "number");
		typeMap.new Type(NumericData.class, "numb");
		typeMap.new Type(TimeData.class, "time");
		typeMap.new Type(BooleanData.class, "boolean");
		typeMap.new Type(BooleanData.class, "bool");
		typeMap.new Type(StringData.class, "string");
		typeMap.new Type(StringData.class, "str");
		typeMap.new Type(StringData.class, "text");
		typeMap.new Type(StringData.class, "txt");
		typeMap.new Type(ProbabilityData.class, "prob");
		typeMap.new Type(ProbabilityData.class, "probability");
	}

	public VariableMap getVariableMap() {
		return variableMap;
	}

	public static Evaluator getEvaluator() {
		return new Evaluator();
	}

	public static Data<?> solve(String input) {
		return new Evaluator().solve(Spate.spate(input));
	}

	public static String solveToString(String input) {
		return solve(input).toStringValue();
	}

	public static BigDecimal solveToNumber(String input) {
		return solveToNumber(Spate.spate(input));
	}

	public static BigDecimal solveToNumber(Spate<Character> input) {
		return new Evaluator().solve(input).toNumericData().evaluate();
	}

	public static void main(final String[] args) {
		final Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine())
			System.out.println(solveToString(scanner.nextLine()));
		scanner.close();
	}

	private Spate<Character> equation;

	private Evaluator() {
	}

	private int box(final Character character) {
		return character == null ? -1 : character;
	}

	/**
	 * Clears any whitespace, and returns true if the next character is the end of
	 * the equation.
	 * 
	 * @param err The error to throw if the end of the equation is found.
	 * @return <code>true</code> if an error was not thrown and the end of the
	 *         equation is found, <code>false</code> if an error was not thrown and
	 *         the end of the equation was not found (i.e. another character that
	 *         isn't whitespace was found).
	 */
	private boolean clearWhitespace(final String err) {
		Character c;
		while (Character.isWhitespace(box(c = equation.peek())))
			equation.skip();
		if (err != null)
			if (c == null)
				throw new RuntimeException(err);
			else
				return false;
		else
			return c == null;

	}

	private List<String> parseFunctionArgs(final StandardWrapper parentheses) {
		// When called, peek() should return an opening parenthesis.

		int meets = 0;
		int c;

		while (true) {
			c = box(equation.next());
			if (c == -1)
				return null;
			else if (c == parentheses.getOpener())
				break;
		}
		meets++;

		final List<String> args = new LinkedList<>();
		String arg = "";
		while (true) {
			c = box(equation.peek());
			if (c == -1)
				throw new RuntimeException("Equation ends prematurely; a closing '" + parentheses.getCloser()
						+ "' was expected for a function, but was not found.");
			else if (c == parentheses.getOpener())
				meets++;
			else if (c == parentheses.getCloser())
				meets--;
			equation.skip();
			if (meets == 0) {
				args.add(arg);
				break;
			} else if (meets == 1 && c == ',') {
				args.add(arg.trim());
				arg = "";
				continue;
			}
			arg += (char) c;
		}

		return args;
	}

	private ChainTerm<?> parseNest(final StandardWrapper parentheses) {

		// Should be called when peek() returns an opening parenthesis.
		int meets = 0;
		String chain = "";
		int c;

		// SKIPS ANY CHARACTERS BEFORE MEETING ITS OPENING PARENTHESIS
		while (true) {
			c = box(equation.next());
			if (c == -1)
				return null;
			else if (c == '"') {
				boolean escaped = false;
				while (true) {
					c = box(equation.peek());
					if (c == '\\')
						escaped ^= true;
					else if (c == '"')
						if (escaped)
							escaped = false;
						else
							break;
					else if (c == -1)
						throw new RuntimeException("No closing quotation mark (\") found while parsing a String.");
					else if (escaped)
						escaped = false;
					equation.skip();
				}
			} else if (c == parentheses.getOpener())
				break;
		}
		meets++;
		while (true) {
			c = box(equation.peek());
			if (c == -1)
				throw new RuntimeException("Equation ends prematurely; a closing '" + parentheses.getCloser()
						+ "' was expected, but was not found.");
			else if (c == '"') {
				boolean escaped = false;
				equation.skip();
				chain += '"';
				while (true) {
					c = box(equation.peek());
					if (c == '\\')
						escaped ^= true;
					else if (c == '"')
						if (escaped)
							escaped = false;
						else
							break;
					else if (c == -1)
						throw new RuntimeException("No closing quotation mark (\") found while parsing a String.");
					else if (escaped)
						escaped = false;
					equation.skip();
					chain += (char) c;
				}
			} else if (c == parentheses.getOpener())
				meets++;
			else if (c == parentheses.getCloser())
				meets--;
			equation.skip();
			if (meets == 0)
				break;
			chain += (char) c;
		}
		return new Evaluator(Spate.spate(chain)).chain();
	}

	private Evaluator(Spate<Character> equation) {
		this.equation = equation;
	}

	private NormalOperator parseOperator() {
		clearWhitespace("The equation ended permaturely; an operator was expected.");

		final int c = equation.peek();
		if (c == '(' || Character.isLetterOrDigit(c) || c == '.' || c == '_' || c == '<')
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
		else if (c == '&') {
			if (equation.peek() == c)
				equation.skip();
			return AND;
		} else if (c == '|') {
			if (equation.peek() == c)
				equation.skip();
			return OR;
		} else if (c == '=') {
			if (equation.peek() == '=')
				equation.skip();
			return EQUAL;
		} else if (c == '<') {
			if (equation.peek() == '=') {
				equation.skip();
				return LESS_THAN_OR_EQUAL_TO;
			} else
				return LESS_THAN;
		} else if (c == '>') {
			if (equation.peek() == '=') {
				equation.skip();
				return GREATER_THAN_OR_EQUAL_TO;
			} else
				return GREATER_THAN;
		} else
			throw new RuntimeException("Could not parse the operator, '" + (char) c + "'");
	}

	public synchronized Data<?> solve(final Spate<Character> equation) {
		this.equation = equation;
		return chain().evaluate();
	}

	/**
	 * This method parses an equation from {@link #equation}. It expects
	 * {@link #equation} to be set. It only parses the actual equation, not any
	 * variable assignments. (If those exist, they should already be parsed when
	 * this method is called.
	 * 
	 * @return Returns a {@link ChainTerm} of a parsed equation.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized ChainTerm<?> chain() {

		// Check to see if the equation is empty.
		Character c;
		while (Character.isWhitespace(box(c = equation.peek())))
			equation.skip();
		if (c == null)
			throw new RuntimeException("Equation has no evaluatable content.");

		final ChainTerm<?> parse = new ChainTerm<>(parseTerm());
		clearWhitespace(null);
		while (equation.hasNext()) {
			parse.append((NormalOperator) parseOperator(), (Term) parseTerm());
			clearWhitespace(null);// For the while loop.
		}
		return parse;
	}

	@SuppressWarnings("unchecked")
	private Term<?> parseTerm() {

		// Check for whitespace. Stop where equation.next() will return the first
		// non-whitepsace char.
		clearWhitespace("The equation ended prematurely; another term was expected.");

		List<Class<? extends Data<?>>> castList = new LinkedList<>();

		Term<?> term;

		int c;
		boolean numericNegation = false, logicalNegation = false;

		TERM_LOOP: while (true) {
			c = box(equation.peek());
			if (c == StandardWrapper.CHEVRONS.getOpener()) {
				equation.skip();// Pass the opening chevron. Next 'peek()' will return the char after it.
				clearWhitespace(
						"Equation ended prematurely; expected a type to cast the following term to. No type, closing chevron: ('>'), nor following term, was found.");
				String type = "";
				while (true) {// Parse the type
					c = box(equation.next());
					if (Character.isLetter(c) || c == '.')
						type += (char) c;
					else if (c == '>') {
						break;
					} else if (Character.isWhitespace(c)) {
						clearWhitespace(
								"Equation ended prematurely; expected a closing chevron, ('>'), to close a cast operation, and then the term that was being casted. Neither of those two things were found.");
						if ((c = equation.next()) != '>')
							throw new RuntimeException(
									"Expected a closing chevron, ('>'), to close a cast operation after some whitespace. Instead, found the character "
											+ c);
						break;
					}
				}

				Class<? extends Data<?>> typeCls = typeMap.get(type);
				if (typeCls == null)
					throw new RuntimeException("Couldn't discern a type from the given reference: " + type + ".");
				castList.add(typeCls);

				continue;// This allows multiple casts to take place.

			} else if (c == '+') {// Force Positive
				numericNegation = false;
				castList.add(NumericData.class);
			} else if (c == '-') {// Flip Negativity
				numericNegation ^= true;
				castList.add(NumericData.class);
			} else if (c == '!') {
				logicalNegation ^= true;
				castList.add(BooleanData.class);
			} else if (c == '"') {
				equation.skip();
				term = Term.wrap(new StringData(parseString()));
				equation.skip();
				break;
			} else if (c == '(') {// Nest
				final ChainTerm<?> nest = parseNest(StandardWrapper.PARENTHESES);
				if (nest == null)
					throw new RuntimeException("Error while parsing some parentheses' content.");
				term = nest;
				break;
			} else if (Character.isLetter(c) || c == '_') {// Variable
				String name = "" + (char) c;
				equation.skip();// equation is now positioned at first function name char
				while ((c = box(equation.peek())) == '_' || Character.isLetterOrDigit(c) || c == ':') {// TODO
																										// Refine
					name += (char) c;
					equation.skip();
				}

				if (name.equalsIgnoreCase("cabbage"))
					throw new RuntimeException("Received cabbage, expected child porn.");

				Boolean isFunction;// true indicates that "function::" prefixing was used, false indicates that
									// such was used, but for variables, and null indicates that no forcing was
									// determined off of prefix detection.
				if (name.startsWith("f::") || name.startsWith("func::") || name.startsWith("function::"))
					isFunction = true;
				else if (name.startsWith("v::") || name.startsWith("var::") || name.startsWith("vars::")
						|| name.startsWith("variables::"))
					if (c == '[')
						throw new RuntimeException(
								"Object name was specified to be the name of a variable, but was invoked as a function. Name: "
										+ name);
					else
						isFunction = false;
				else
					isFunction = null;
				if (isFunction != null && (name = name.substring(name.indexOf("::") + 2)).isEmpty())
					throw new RuntimeException("No " + (isFunction ? "function" : "variable") + " name specified.");

				// TODO Match name against name database to determine if it's a function or
				// variable.

				// Functions have a higher precedence than vars; if there is a name conflict, a
				// the name will be parsed as the function rather than a variable, unless the
				// "function::functionName" specifying syntax is used, or a tilde is used to
				// force variable treatment.

				@SuppressWarnings("rawtypes")
				final SimpleFunction function = SimpleFunction.getFunction(name);

				if (isFunction == null)// The indication by isFunction changes here.
					isFunction = function == null ? false : true;

				if (isFunction) {
					StandardWrapper parenthesis;
					if (c == -1 || (parenthesis = StandardWrapper.openValueOf((char) c)) == null)
						throw new RuntimeException(
								name + " was determined to be a function, but was not followed by parentheses.");

					final List<String> args = parseFunctionArgs(parenthesis);
					if (function == null)
						throw new RuntimeException("Invalid function name: " + name
								+ "; couldn't find a function with the specified name.");
					else if (args.isEmpty())
						throw new RuntimeException("Not enough arguments given for the function: " + name + ".");
					else if (args.size() > 1)
						throw new RuntimeException("Excessive arguments passed to function, " + name + ".");
					term = function.evaluate(new Evaluator(Spate.spate(args.get(0))).chain().evaluate());
					break;
				} else {
					if (c == '[')
						throw new RuntimeException("Brackets were used to designate that " + name
								+ " should be a function, but a function wasn't found with that name. Perhaps it's a variable and parentheses were meant to be used instead.");
					Variable<?> variable = variableMap.getVariable(name);
					if (variable == null)
						throw new RuntimeException("Invalid variable name: " + name
								+ "; couldn't find a variable with the specified name.");
					term = variable::getValue;
					break;
				}

			} else if (Character.isDigit(c) || c == '.') {// Parse Number or Time
				String content = "";
				OUTER: while (true) {
					c = box(equation.peek());
					if (c == '.') {// Parse Number
						content += '.';
						equation.skip();
						while (true) {
							c = box(equation.peek());
							if (Character.isDigit(c))
								content += (char) c;
							else if (c == '.')
								throw new RuntimeException("Encountered multiple decimal points in a number.");
							else
								break OUTER;
							equation.skip();
						}
					} else if (Character.isDigit(c))
						content += (char) c;
					else if (c == ':') {// Parse Time
						while (true) {
							c = box(equation.peek());
							if (Character.isDigit(c))
								content += (char) c;
							else if (c == ':')
								if (content.charAt(content.length() - 1) == ':')
									throw new RuntimeException(
											"Duplicate, tandem colon found while parsing a time value.");
								else
									content += ':';
							else {
								term = Term.wrap(new TimeData(content));
								break TERM_LOOP;
							}

							equation.skip();
						}
					} else
						break;
					equation.skip();
				}

				if (content.charAt(content.length() - 1) == '.')
					throw new RuntimeException("Unnecessary decimal found.");
				term = new org.alixia.libs.evaluator.api.terms.Number(
						new NumericData(new BigDecimal(content).multiply(new BigDecimal(numericNegation ? -1 : 1))));
				break TERM_LOOP;

			} else if (c == -1)
				throw new RuntimeException("Expected a term but found the end of the equation.");
			else if (!Character.isWhitespace(c))
				throw new RuntimeException("Unexpected character while parsing a term: " + (char) c);
			equation.skip();
		} // Leaves off before first digit.

		// Apply casts in order.
		for (int i = castList.size() - 1; i >= 0; i--)
			term = Term.castTerm((Term<Data<?>>) term, (Class<Data<Object>>) castList.get(i));

		while (box(equation.peek()) == '!') {
			equation.skip();
			term = Term.factorial(term);
		}

		// TODO Check to see if the term is actually of the type boolean. Otherwise,
		// throw an exception.
		if (logicalNegation)
			term = Term.not((Term<? extends Data<? extends Boolean>>) term);

		return term;
	}

	/**
	 * <p>
	 * Starts where {@link #equation}'s {@link Spate#peek()} will return the first
	 * character <b>inside</b> the string to be parsed.
	 * <p>
	 * Handles escaped quotations and escaped backslashes using backslashes as
	 * escape chars.
	 * 
	 * @return The parse {@link String}.
	 */
	private String parseString() {
		String string = "";
		int c;
		boolean escaped = false;
		while (true) {
			c = box(equation.peek());
			if (c == '\\')
				if (escaped) {
					string += '\\';
					escaped = false;
				} else
					escaped = true;
			else if (c == '"')
				if (escaped) {
					string += '"';
					escaped = false;
				} else
					break;
			else if (c == -1)
				throw new RuntimeException("No closing quotation mark (\") found while parsing a String.");
			else {
				if (escaped) {
					string += '\\';
					escaped = false;
				}
				string += (char) c;
			}
			equation.skip();
		}

		return string;
	}

	public static BigDecimal roundBigDecimal_old(String decimal) {
		int i = decimal.indexOf('.');
		if (i == -1)
			return new BigDecimal(decimal);
		int firstZero = -1;
		for (; i < decimal.length(); i++) {
			if (firstZero == -1)
				if (decimal.charAt(i) == '0')
					firstZero = i;
				else
					;
			else if (decimal.charAt(i) != '0')
				firstZero = -1;
		}
		return new BigDecimal(firstZero == -1 ? decimal : decimal.substring(0, firstZero));
	}

	/**
	 * Strips the trailing zeros off of a {@link BigDecimal}, and returns a new
	 * {@link BigDecimal} representing the old one without the extra zeros.
	 * 
	 * @param decimal The decimal to strip.
	 * @return A new {@link BigDecimal} without excess zeroes.
	 * 
	 *         Yes this function is named badly.
	 */
	public static BigDecimal roundBigDecimal(BigDecimal decimal) {
		return decimal.stripTrailingZeros();
	}

	public static BigDecimal roundBigDecimal(String decimal) {
		return roundBigDecimal(new BigDecimal(decimal));
	}

	public static BigDecimal divideSafely(BigDecimal first, BigDecimal second) {
		return roundBigDecimal(first.divide(second, MAXIMUM_BIG_DECIMAL_DIVISION_SCALE, RoundingMode.HALF_UP));
	}

}
