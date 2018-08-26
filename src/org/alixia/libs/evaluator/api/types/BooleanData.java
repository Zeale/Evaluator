package org.alixia.libs.evaluator.api.types;

public class BooleanData extends SimpleData<Boolean> {

	public BooleanData(Boolean value) {
		super(value);
	}

	/**
	 * Constructs a {@link BooleanData} object, by calling
	 * {@link #BooleanData(Boolean)} and passing in <code>null</code>, NOT
	 * <code>false</code>, as some may expect.
	 */
	public BooleanData() {
		this(null);
	}

	@Override
	public <DT extends Data<?>> Data<Boolean> cast(DT item) {
		BooleanData data = new BooleanData();
		data.fromNumericData(item.toNumericData());
		return data;
	}

	@Override
	public NumericData toNumericData() {
		return new NumericData(value ? 1 : 0);
	}

	@Override
	public void fromNumericData(NumericData data) {
		value = data.evaluate().doubleValue() != 0;
	}

	@Override
	public String toStringValue() {
		return value + "";
	}

	@Override
	public SimpleData<Boolean> clone() {
		return new BooleanData(evaluate());
	}

}
