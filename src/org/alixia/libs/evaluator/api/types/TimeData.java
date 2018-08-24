package org.alixia.libs.evaluator.api.types;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeData extends SimpleData<LocalDateTime> {

	// TODO Fix (i.e. apply new LocalDateTime stuff instead of using Instant stuff).

	public TimeData(LocalDateTime value) {
		super(value);
	}

	public TimeData(String value) {
		this(value == null ? null : LocalDateTime.parse(value));
	}

	@Override
	public <DT extends Data<?>> TimeData cast(DT item) {
		TimeData data = new TimeData((LocalDateTime) null);
		data.fromNumericData(item.toNumericData());
		return data;
	}

	@Override
	public NumericData toNumericData() {
		return new NumericData(value.toEpochSecond(ZoneOffset.ofHours(0)) + "." + value.getNano());
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
		value = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	@Override
	public TimeData clone() {
		return new TimeData(value);
	}

}
