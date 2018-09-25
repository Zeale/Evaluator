package org.alixia.libs.evaluator.api.types;

import java.math.BigDecimal;

public class ProbabilityData extends SimpleData<BigDecimal> {

	public ProbabilityData() {
		super(null);
	}

	public ProbabilityData(BigDecimal value) {
		super(value.compareTo(BigDecimal.ONE) == 1 ? BigDecimal.ONE
				: value.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : value);
	}

	public ProbabilityData(Data<?> data) {
		super(null);
		fromNumericData(data.toNumericData());
	}

	@Override
	public <DT extends Data<?>> Data<BigDecimal> cast(DT item) {
		return new ProbabilityData(item);
	}

	@Override
	public NumericData toNumericData() {
		return new NumericData(value);
	}

	@Override
	public void fromNumericData(NumericData data) {
		value = data.value.compareTo(BigDecimal.ONE) == 1 ? BigDecimal.ONE
				: data.value.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : data.value;
	}

	@Override
	public String toStringValue() {
		return value.toString() + "%";
	}

	@Override
	public ProbabilityData clone() {
		return new ProbabilityData(value);
	}

}
