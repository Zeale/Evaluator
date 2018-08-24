package org.alixia.libs.evaluator.api.types;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeData extends SimpleData<LocalDateTime> {

	// TODO Fix (i.e. apply new LocalDateTime stuff instead of using Instant stuff).

	public TimeData(LocalDateTime value) {
		super(value);
	}

	public TimeData(String value) {
		this(value == null ? null : parse(value));
	}

	public static LocalDateTime parse(String value) {
		String[] times = value.split(":");
		if (times.length == 7)
			return LocalDateTime.of(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]),
					Integer.parseInt(times[3]), Integer.parseInt(times[4]), Integer.parseInt(times[5]),
					Integer.parseInt(times[6]));
		else if (times.length == 6)
			return LocalDateTime.of(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]),
					Integer.parseInt(times[3]), Integer.parseInt(times[4]), Integer.parseInt(times[5]));
		else if (times.length == 5)
			return LocalDateTime.of(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]),
					Integer.parseInt(times[3]), Integer.parseInt(times[4]));
		// TODO Improve this.
		throw new RuntimeException("Invalid number of time parts. (Input: " + value + ")");
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
