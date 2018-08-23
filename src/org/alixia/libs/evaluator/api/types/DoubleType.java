package org.alixia.libs.evaluator.api.types;

import java.math.BigDecimal;

public class DoubleType implements Type<Double> {
	public static final DoubleType INSTANCE = new DoubleType();

	private DoubleType() {
	}

	@Override
	public Double fromString(String string) {
		return Double.parseDouble(string);
	}

	@Override
	public BigDecimal toBigDecimal(Double value) {
		return new BigDecimal(value);
	}

	@Override
	public Double fromBigDecimal(BigDecimal value) {
		return value.doubleValue();
	}
}
