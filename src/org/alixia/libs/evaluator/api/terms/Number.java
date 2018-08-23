package org.alixia.libs.evaluator.api.terms;

import java.math.BigDecimal;

import org.alixia.libs.evaluator.api.types.NumericData;

public class Number implements Term<NumericData> {
	private final NumericData value;

	public Number(final NumericData value) {
		if (value == null)
			throw new IllegalArgumentException("Numbers can't have null values.");
		this.value = value;
	}

	public Number(BigDecimal value) {
		this(new NumericData(value));
	}

	public Number(java.lang.Number value) {
		this(new BigDecimal(value.toString()));
	}

	@Override
	public NumericData evaluate() {
		return value;
	}

	@Override
	public String toString() {
		return evaluate().toString();
	}

}
