package org.alixia.libs.evaluator.api.types;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

public class TimeData extends SimpleData<LocalDateTime> {

	public final static TimeData getTimeZero() {
		return new TimeData(LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0));
	}

	private final static int[] TIME_ZERO_FRAGMENT_ARRAY = getTimeZero().toArray();

	// TODO Fix (i.e. apply new LocalDateTime stuff instead of using Instant stuff).

	public TimeData(LocalDateTime value) {
		super(value);
	}

	public int[] toArray() {
		LocalDateTime time = evaluate();
		return new int[] { time.getYear(), time.getMonthValue(), time.getDayOfMonth(), time.getHour(), time.getMinute(),
				time.getSecond(), time.getNano() };
	}

	public TimeData(String value) {
		this(value == null ? null : parse(value));
	}

	public static final LocalDateTime parseOld(String value) {
		String[] times = value.split(":");

		int[] timeValues = TIME_ZERO_FRAGMENT_ARRAY;
		if (times.length > timeValues.length)
			throw new RuntimeException("Found too many time fragments while parsing a time. (Input: " + value + ")");
		for (int i = times.length - 1; i >= 0; i--)
			timeValues[i + (timeValues.length - times.length)] = Integer.parseInt(times[i]);

		return LocalDateTime.of(timeValues[0], timeValues[1], timeValues[2], timeValues[3], timeValues[4],
				timeValues[5], timeValues[6]);
	}

	public static LocalDateTime parse(String value) {
		String[] times = value.split(":");
		int[] values = new int[times.length];
		for (int i = 0; i < times.length; i++)
			values[i] = Integer.parseInt(times[i]);
		return parse(values);
	}

	private final static int MONTH_THRESHOLD = 12, DAY_THRESHOLD = 31, HOUR_THRESHOLD = 23, SECOND_THRESHOLD = 59;

	private static LocalDateTime parse(int[] times) {
		if (times == null || times.length == 0)
			throw new RuntimeException("Can't parse an empty time fragment array.");
		// 1920:5:3 is 5/3/1920-00:00:00:000:000:000
		// The above is because "1920" is too large to be an hour, a day, or a month, so
		// we know it must be a year. Following the "year", is always the month, then
		// the day, then the hour, etc.

		// 1:3 is EPOCH-00:01:03:000:000:000
		// This results because the right-most value is always parsed to be a second,
		// unless we know that it can't be, in which case values are shifted to the left
		// in the time units line. In this case, one is a valid minute, and three is
		// a valid second, so this evaluates to a total of 63 seconds (or one minute and
		// three seconds). If the one was actually a 1340, we would know that it'd have
		// to be a year. That would make the three a month.
		//
		// The string:
		// 18:5:2:3:4
		// will equate to the year 18, month five, day two, hour three, and minute four.
		// (Ideally, if 18 wasn't so big, 18 would be parsed as a month, but since it's
		// too big to be a month, we shift everything to the left and parse it as a year
		// (NOT shift to the right, and parse it as a day)).
		int[] timeValues = new int[] { 1970, 1, 1, 0, 0, 0, 0 };

		if (times.length > timeValues.length)
			throw new RuntimeException("Found too many time fragments while parsing a time. (Parsed Input: "
					+ Arrays.toString(times) + ")");

		// To set yrs, shift must = 0.
		final byte standardShift = (byte) (timeValues.length - times.length - (times.length == 7 ? 0 : 1));
		byte shift = standardShift;// We try to parse so that the last value in times represents a second.

		// YEAR, MONTH-12, DAY-31, HOUR-24, MINUTE-60, SECOND-60, NANOSECOND

		if (times.length > 5 || times[0] > SECOND_THRESHOLD)
			shift = 0;
		else {
			// times.length<6 && times[0] !> 59
			//
			// The default value for shift is such that the last number is the "seconds"
			// value.
			if (times.length == 5) {
				if (times[0] > MONTH_THRESHOLD || times[2] > HOUR_THRESHOLD)
					shift = 0;
			} else if (times.length == 4) {
				if (times[0] > DAY_THRESHOLD)// shift -= 2, regardless of other values.
					shift = 0;
			} else if (times.length == 3) {
				if (times[0] > HOUR_THRESHOLD)
					shift -= times[0] > DAY_THRESHOLD ? 3 : 1;
			} else if (times.length <= 2) {
				if (times[0] > SECOND_THRESHOLD)// SECOND_THRESH... is the same as minute's threshold
					shift = 0;
			}
		}

		int overhead = standardShift - shift;
		if (overhead < 0)
			throw new RuntimeException("Received " + overhead + " extra time fragments while parsing a time.");
		for (int i = 0; i < times.length; i++)
			timeValues[i + shift] = times[i];

		try {
			return LocalDateTime.of(timeValues[0], timeValues[1], timeValues[2], timeValues[3], timeValues[4],
					timeValues[5], timeValues[6]);
		} catch (DateTimeException e) {
			throw new RuntimeException("Invalid values were supplied for a time." + e.getMessage(), e);
		}
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
