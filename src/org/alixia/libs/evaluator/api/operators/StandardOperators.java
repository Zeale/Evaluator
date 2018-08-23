package org.alixia.libs.evaluator.api.operators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

import org.alixia.libs.evaluator.Evaluator;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.Data;
import org.alixia.libs.evaluator.api.types.NumericData;

public enum StandardOperators implements NormalOperator, Precedented {
	ADD((BigDecimalHandler) BigDecimal::add, 1), SUBTRACT((BigDecimalHandler) BigDecimal::subtract, 1),
	MULTIPLY((BigDecimalHandler) BigDecimal::multiply, 2), DIVIDE((BigDecimalHandler) (f, s) -> {
		return roundBigDecimal(
				f.divide(s, Evaluator.MAXIMUM_BIG_DECIMAL_DIVISION_SCALE, RoundingMode.HALF_UP).toString());
	}, 2), EXPONENTIATION((BigDecimalHandler) (a, b) -> a.pow(b.intValue()), 3),
	MODULUS((BigDecimalHandler) BigDecimal::remainder, 2);

	private final BiFunction<Data<?>, Data<?>, Data<?>> function;

	private final Precedence precedence;

	private StandardOperators(final BiFunction<Data<?>, Data<?>, Data<?>> function, final int precedence) {
		this.function = function;
		this.precedence = new Precedence(precedence);
	}

	@Override
	public Term<?> evaluate(final Term<? extends Data<?>> first, final Term<? extends Data<?>> second) {
		return Term.wrap(function.apply(first.evaluate(), second.evaluate()));
	}

	@Override
	public Precedence precedence() {
		return precedence;
	}

	private interface BigDecimalHandler extends BiFunction<Data<?>, Data<?>, Data<?>> {
		@Override
		default Data<?> apply(Data<?> t, Data<?> u) {
			return new NumericData(apply(t.toNumericData().evaluate(), u.toNumericData().evaluate()));
		}

		BigDecimal apply(BigDecimal first, BigDecimal second);
	}

	public static BigDecimal roundBigDecimal(String decimal) {
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

}
