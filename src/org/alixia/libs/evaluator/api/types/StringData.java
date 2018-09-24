package org.alixia.libs.evaluator.api.types;

public class StringData extends SimpleData<String> {

	public StringData(String value) {
		super(value);
	}

	public StringData(Data<?> data) {
		super(data == null ? null : data.toStringValue());
	}

	public StringData() {
		this((String) null);
	}

	@Override
	public <DT extends Data<?>> Data<String> cast(DT item) {
		return new StringData(item);
	}

	@Override
	public NumericData toNumericData() {
		try {
			return new NumericData(value);
		} catch (NumberFormatException e) {
			return new NumericData(value.length());
		}
	}

	@Override
	public void fromNumericData(NumericData data) {
		value = data.toStringValue();
	}

	@Override
	public String toStringValue() {
		return value;
	}

	@Override
	public StringData clone() {
		return new StringData(value);
	}

}
