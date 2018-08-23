package org.alixia.libs.evaluator.api.types;

import java.math.BigDecimal;

public final class NumericData extends SimpleData<BigDecimal> {

	public NumericData(BigDecimal value) {
		super(value);
	}

	public NumericData(Number value) {
		this(value.toString());
	}

	public NumericData(String value) {
		this(new BigDecimal(value));
	}

	@Override
	public <DT extends Data<?>> BigDecimal cast(DT item) {
		return null;
	}

	/**
	 * Returns this object.
	 * 
	 * @return This object.
	 */
	@Override
	public NumericData toNumericData() {
		return this;
	}

	/**
	 * Sets this {@link NumericData} to be equal to the specified
	 * {@link NumericData}.
	 * 
	 * @param data The specified {@link NumericData}.
	 */
	@Override
	public void fromNumericData(NumericData data) {
		this.value = data.value;
	}

	@Override
	public NumericData clone() {
		return new NumericData(value);
	}

}
