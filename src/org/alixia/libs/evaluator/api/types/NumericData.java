package org.alixia.libs.evaluator.api.types;

import java.math.BigDecimal;

public final class NumericData extends SimpleData<BigDecimal> {

	public NumericData(BigDecimal value) {
		super(value);
	}

	public NumericData() {
		this((BigDecimal) null);
	}

	public NumericData(Number value) {
		this(value == null ? null : value.toString());
	}

	public NumericData(String value) {
		this(value == null ? null : new BigDecimal(value));
	}

	@Override
	public <DT extends Data<?>> NumericData cast(DT item) {
		return item.toNumericData();
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

	@Override
	public String toStringValue() {
		return value.toString();
	}

}
