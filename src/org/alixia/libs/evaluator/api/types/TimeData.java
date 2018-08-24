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

		int[] timeValues = new int[] { 1970, 1, 1, 0, 0, 0, 0 };
		if (times.length > timeValues.length)
			throw new RuntimeException("Found too many time fragments while parsing a time. (Input: " + value + ")");
		for (int i = times.length - 1; i >= 0; i--)
			timeValues[i + (timeValues.length - times.length)] = Integer.parseInt(times[i]);

		return LocalDateTime.of(timeValues[0], timeValues[1], timeValues[2], timeValues[3], timeValues[4],
				timeValues[5], timeValues[6]);
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

	@Override
	public String toStringValue() {
		return value.getYear() + ":" + value.getMonthValue() + ":" + value.getDayOfMonth() + ":" + value.getHour() + ":"
				+ value.getMinute() + ":" + value.getSecond() + ":" + value.getNano();
	}

}
