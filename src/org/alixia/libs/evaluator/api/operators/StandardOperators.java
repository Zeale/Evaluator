package org.alixia.libs.evaluator.api.operators;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import org.alixia.libs.evaluator.api.terms.Number;
import org.alixia.libs.evaluator.api.terms.Term;

public enum StandardOperators implements NormalOperator<BigDecimal, BigDecimal, BigDecimal>, Precedented {
	ADD((a, b) -> a.add(b), 1), SUBTRACT((a, b) -> a.subtract(b), 1), MULTIPLY((a, b) -> a.multiply(b), 2),
	DIVIDE((a, b) -> a.divide(b), 2), EXPONENTIATION((a, b) -> a.pow(b.intValue()), 3),
	MODULUS((a, b) -> a.remainder(b), 2);

	private final BiFunction<BigDecimal, BigDecimal, BigDecimal> function;

	private final Precedence precedence;

	private StandardOperators(final BiFunction<BigDecimal, BigDecimal, BigDecimal> function, final int precedence) {
		this.function = function;
		this.precedence = new Precedence(precedence);
	}

	@Override
	public Term<BigDecimal> evaluate(final Term<BigDecimal> first, final Term<BigDecimal> second) {
		return new Number<>(function.apply(first.evaluate(), second.evaluate()));
	}

	@Override
	public Precedence precedence() {
		return precedence;
	}

}
