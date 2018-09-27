package org.alixia.libs.evaluator.api.types;

import java.math.BigDecimal;

public class NumberMatrixData extends SimpleData<BigDecimal[][]> {

	public NumberMatrixData(BigDecimal[]... value) {
		super(value);
	}

	public NumberMatrixData() {
		super(null);
	}

	@Override
	public <DT extends Data<?>> NumberMatrixData cast(DT item) {
		if (item instanceof NumberMatrixData)
			return (NumberMatrixData) item;
		NumberMatrixData data = new NumberMatrixData();
		data.fromNumericData(item.toNumericData());
		return data;
	}

	@Override
	public NumericData toNumericData() {
		throw new RuntimeException("A Matrix cannot be cast to a number...");
	}

	@Override
	public void fromNumericData(NumericData data) {
		value = new BigDecimal[][] { { data.evaluate() } };
	}

	@Override
	public String toStringValue() {
		String value = "{";
		BigDecimal[][] arr = this.value;
		if (arr.length > 0) {
			for (int i = 0; i < arr.length - 1; i++) {
				BigDecimal[] bdArr = arr[i];
				value += "{";
				if (bdArr.length > 0) {
					for (int j = 0; j < bdArr.length - 1; j++)
						value += bdArr[j] + ", ";
					value += bdArr[bdArr.length - 1];
				}
				value += "}, ";
			}

			value += "{";
			BigDecimal[] bdArr = arr[arr.length - 1];
			if (bdArr.length > 0) {
				for (int j = 0; j < bdArr.length - 1; j++)
					value += bdArr[j] + ", ";
				value += bdArr[bdArr.length - 1];
			}
			value += "}";

		}
		value += "}";

		return value;
	}

	public static void main(String[] args) {
		System.out.println(new NumberMatrixData(
				new BigDecimal[][] { { new BigDecimal(5) }, { new BigDecimal(5), new BigDecimal(5) } }));
	}

	@Override
	public NumberMatrixData clone() {
		return new NumberMatrixData(value);
	}

}
