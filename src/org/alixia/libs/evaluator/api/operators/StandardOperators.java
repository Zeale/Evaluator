package org.alixia.libs.evaluator.api.operators;

import java.util.function.BiFunction;

import org.alixia.libs.evaluator.api.terms.Number;
import org.alixia.libs.evaluator.api.terms.Term;

public enum StandardOperators implements NormalOperator<Double, Double, Double> {
	ADD((a, b) -> a + b), SUBTRACT((a, b) -> a - b), MULTIPLY((a, b) -> a * b), DIVIDE((a, b) -> a / b), EXPONENTIATION(
			Math::pow), MODULUS((a, b) -> a % b);

	private final BiFunction<Double, Double, Double> function;

	private StandardOperators(BiFunction<Double, Double, Double> function) {
		this.function = function;
	}

	@Override
	public Term<Double> evaluate(Term<Double> first, Term<Double> second) {
		return new Number<Double>(function.apply(first.evaluate(), second.evaluate()));
	}

}
