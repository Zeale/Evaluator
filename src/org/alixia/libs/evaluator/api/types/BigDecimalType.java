package org.alixia.libs.evaluator.api.types;

import java.math.BigDecimal;

public class BigDecimalType implements Type<BigDecimal> {

	public static final BigDecimalType INSTANCE = new BigDecimalType();

	private BigDecimalType() {
	}

	@Override
	public BigDecimal fromString(String string) {
		return new BigDecimal(string);
	}

	@Override
	public BigDecimal toBigDecimal(BigDecimal value) {
		return value;
	}

	@Override
	public BigDecimal fromBigDecimal(BigDecimal value) {
		return value;
	}

}
