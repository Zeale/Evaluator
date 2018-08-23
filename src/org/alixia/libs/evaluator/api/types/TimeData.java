package org.alixia.libs.evaluator.api.types;

import java.time.Instant;

public class TimeData extends SimpleData<Instant> {

	public TimeData(Instant value) {
		super(value);
	}

	public TimeData(String value) {
		this(value == null ? null : Instant.parse(value));
	}

	@Override
	public <DT extends Data<?>> TimeData cast(DT item) {
		TimeData data = new TimeData((Instant) null);
		data.fromNumericData(item.toNumericData());
		return data;
	}

	@Override
	public NumericData toNumericData() {
		return new NumericData(value.toEpochMilli() + "." + value.getNano());
	}

	@Override
	public void fromNumericData(NumericData data) {
		String time = data.evaluate() + "";
		int endMillis = time.indexOf('.') == -1 ? time.length() : time.indexOf('.');
		long millis = Long.parseLong(time.substring(0, endMillis));
		// TODO Improve.
		Instant instant = time.contains(".")
				? Instant.ofEpochSecond(millis / 1000, Long.parseLong(time.substring(endMillis + 1)))
				: Instant.ofEpochMilli(millis);
		value = instant;
	}

	@Override
	public TimeData clone() {
		return new TimeData(value);
	}

}
