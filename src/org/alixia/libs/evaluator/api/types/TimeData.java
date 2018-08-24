package org.alixia.libs.evaluator.api.types;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeData extends SimpleData<LocalDateTime> {

	// TODO Fix (i.e. apply new LocalDateTime stuff instead of using Instant stuff).

	public TimeData(LocalDateTime value) {
		super(value);
	}

	public static void main(String[] args) {
		TimeData td = new TimeData((LocalDateTime) null);
		td.fromNumericData(new NumericData(200000.887698798));
		System.out.println(td.evaluate());
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
		int endSecs = time.indexOf('.') == -1 ? time.length() : time.indexOf('.');
		long secs = Long.parseLong(time.substring(0, endSecs));
		// TODO Improve.
		int endpoint = time.length() - endSecs >= 10 ? endSecs + 10 : time.length();
		try {
			value = LocalDateTime.ofEpochSecond(secs,
					time.contains(".") ? Integer.parseInt(time.substring(endSecs + 1, endpoint)) : 0,
					ZoneOffset.ofHours(0));
		} catch (DateTimeException | NumberFormatException e) {
			throw new RuntimeException(
					"The given number is too large to be converted into a time. (Number: " + time + ")");
		}
	}

	@Override
	public TimeData clone() {
		return new TimeData(value);
	}

}
