package org.alixia.libs.evaluator.api.types;

import java.math.BigDecimal;

public interface Type<T> {
	T fromString(String string);

	BigDecimal toBigDecimal(T value);

	T fromBigDecimal(BigDecimal value);
}
