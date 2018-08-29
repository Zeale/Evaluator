package org.alixia.libs.evaluator.api.types;

import java.util.function.BiFunction;

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

	public interface BiBoolFunction extends BiFunction<Data<?>, Data<?>, Data<?>> {
		@Override
		default Data<?> apply(Data<?> t, Data<?> u) {
			return applyBool(Data.cast(t, BooleanData.class), Data.cast(u, BooleanData.class));
		}

		<FT extends BooleanData, ST extends BooleanData> Data<?> applyBool(FT t, ST u);
	}

	@Override
	public <DT extends Data<?>> Data<Boolean> cast(DT item) {
		BooleanData data = new BooleanData();
		data.fromNumericData(item.toNumericData());
		return data;
	}

	public BooleanData and(BooleanData bool) {
		return new BooleanData(evaluate() && bool.evaluate());
	}

	public BooleanData not() {
		return new BooleanData(!evaluate());
	}

	public BooleanData or(BooleanData bool) {
		return new BooleanData(evaluate() || bool.evaluate());
	}

	public BooleanData xor(BooleanData bool) {
		return new BooleanData(evaluate() ^ bool.evaluate());
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
