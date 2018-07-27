package org.alixia.libs.evaluator.api.operators;

import java.util.function.BiFunction;

import org.alixia.libs.evaluator.api.terms.Number;
import org.alixia.libs.evaluator.api.terms.Term;

public enum StandardOperators implements NormalOperator<Double, Double, Double>, Precedented {
	ADD((a, b) -> a + b, 1), SUBTRACT((a, b) -> a - b, 1), MULTIPLY((a, b) -> a * b, 2), DIVIDE((a, b) -> a / b,
			2), EXPONENTIATION(Math::pow, 3), MODULUS((a, b) -> a % b, 2);

	private final BiFunction<Double, Double, Double> function;

	private StandardOperators(BiFunction<Double, Double, Double> function, int precedence) {
		this.function = function;
		this.precedence = precedence;
	}

	@Override
	public Term<Double> evaluate(Term<Double> first, Term<Double> second) {
		return new Number<Double>(function.apply(first.evaluate(), second.evaluate()));
	}

	private final int precedence;

	@Override
	public int precedence() {
		return precedence;
	}

}
