package org.alixia.libs.evaluator.api.time;

import static org.alixia.libs.evaluator.Evaluator.divideSafely;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Time {

	public static void main(String[] args) {
		System.out.println(TimeUnit.YEARS.toDays(new BigDecimal("2")).doubleValue());
	}

	private interface TimeUnitConvertible {
		BigDecimal toPicoseconds(BigDecimal amount);

		BigDecimal toNanoseconds(BigDecimal amount);

		BigDecimal toMicroseconds(BigDecimal amount);

		BigDecimal toMilliseconds(BigDecimal amount);

		BigDecimal toSeconds(BigDecimal amount);

		BigDecimal toMinutes(BigDecimal amount);

		BigDecimal toHours(BigDecimal amount);

		BigDecimal toDays(BigDecimal amount);

		BigDecimal toWeeks(BigDecimal amount);

		BigDecimal toMonths(BigDecimal amount);

		BigDecimal toYears(BigDecimal amount);

		BigDecimal toDecades(BigDecimal amount);

		BigDecimal toCenturies(BigDecimal amount);

		BigDecimal toMillenia(BigDecimal amount);

		BigDecimal toEons(BigDecimal amount);

	}

	private static final long NANOSECOND_TO_PICOSECOND_MULTIPLIER = 1000, MICROSECOND_TO_NANOSECOND_MULTIPLIER = 1000,
			MILLISECOND_TO_MICROSECOND_MULTIPLIER = 1000, SECOND_TO_MILLISECOND_MULTIPLIER = 1000,
			MINUTE_TO_SECOND_MULTIPLIER = 60, HOUR_TO_MINUTE_MULTIPLIER = 60, DAY_TO_HOUR_MULTIPLIER = 24,
			WEEK_TO_DAY_MULTIPLIER = 7, YEAR_TO_MONTH_MULTIPLIER = 12, YEAR_TO_DAY_MULTIPLIER = 365,
			DECADE_TO_YEAR_MULTIPLIER = 10, CENTURY_TO_DECADE_MULTIPLIER = 10, MILLENIUM_TO_CENTURY_MULTIPLIER = 10,
			EON_TO_MILLENIUM_MULTIPLIER = 10;

	public enum TimeUnit implements TimeUnitConvertible {
		PICOSECONDS(new TimeUnitConvertible() {

			@Override
			public BigDecimal toPicoseconds(BigDecimal amount) {
				return amount;
			}

			@Override
			public BigDecimal toNanoseconds(BigDecimal amount) {
				return amount.divide(new BigDecimal(NANOSECOND_TO_PICOSECOND_MULTIPLIER));
			}

			@Override
			public BigDecimal toMicroseconds(BigDecimal amount) {
				return toNanoseconds(amount).divide(new BigDecimal(MICROSECOND_TO_NANOSECOND_MULTIPLIER));
			}

			@Override
			public BigDecimal toMilliseconds(BigDecimal amount) {
				return toMicroseconds(amount).divide(new BigDecimal(MILLISECOND_TO_MICROSECOND_MULTIPLIER));
			}

			@Override
			public BigDecimal toSeconds(BigDecimal amount) {
				return toMilliseconds(amount).divide(new BigDecimal(SECOND_TO_MILLISECOND_MULTIPLIER));
			}

			@Override
			public BigDecimal toMinutes(BigDecimal amount) {
				return divideSafely(amount, new BigDecimal(MINUTE_TO_SECOND_MULTIPLIER));
			}

			@Override
			public BigDecimal toHours(BigDecimal amount) {
				return divideSafely(amount, new BigDecimal(HOUR_TO_MINUTE_MULTIPLIER));
			}

			@Override
			public BigDecimal toDays(BigDecimal amount) {
				return divideSafely(amount, new BigDecimal(DAY_TO_HOUR_MULTIPLIER));
			}

			@Override
			public BigDecimal toWeeks(BigDecimal amount) {
				return divideSafely(amount, new BigDecimal(WEEK_TO_DAY_MULTIPLIER));
			}

			@Override
			public BigDecimal toMonths(BigDecimal amount) {
				return toYears(amount).multiply(new BigDecimal(YEAR_TO_MONTH_MULTIPLIER));
			}

			@Override
			public BigDecimal toYears(BigDecimal amount) {
				return divideSafely(amount, new BigDecimal(YEAR_TO_DAY_MULTIPLIER));
			}

			@Override
			public BigDecimal toDecades(BigDecimal amount) {
				return toYears(amount).divide(new BigDecimal(DECADE_TO_YEAR_MULTIPLIER));
			}

			@Override
			public BigDecimal toCenturies(BigDecimal amount) {
				return toDecades(amount).divide(new BigDecimal(CENTURY_TO_DECADE_MULTIPLIER));
			}

			@Override
			public BigDecimal toMillenia(BigDecimal amount) {
				return toCenturies(amount).divide(new BigDecimal(MILLENIUM_TO_CENTURY_MULTIPLIER));
			}

			@Override
			public BigDecimal toEons(BigDecimal amount) {
				return toMillenia(amount).divide(new BigDecimal(EON_TO_MILLENIUM_MULTIPLIER));
			}
		}), NANOSECONDS(new EasyConverter(PICOSECONDS, new BigDecimal(NANOSECOND_TO_PICOSECOND_MULTIPLIER))),
		MICROSECONDS(NANOSECONDS, MICROSECOND_TO_NANOSECOND_MULTIPLIER),
		MILLISECONDS(MICROSECONDS, MILLISECOND_TO_MICROSECOND_MULTIPLIER),
		SECONDS(MILLISECONDS, SECOND_TO_MILLISECOND_MULTIPLIER), MINUTES(SECONDS, MINUTE_TO_SECOND_MULTIPLIER),
		HOURS(MINUTES, HOUR_TO_MINUTE_MULTIPLIER), DAYS(HOURS, DAY_TO_HOUR_MULTIPLIER),
		WEEKS(DAYS, WEEK_TO_DAY_MULTIPLIER), YEARS(DAYS, YEAR_TO_DAY_MULTIPLIER),
		MONTHS(YEARS, 1d / YEAR_TO_MONTH_MULTIPLIER), DECADES(YEARS, DECADE_TO_YEAR_MULTIPLIER),
		CENTURIES(DECADES, CENTURY_TO_DECADE_MULTIPLIER), MILLENIA(CENTURIES, MILLENIUM_TO_CENTURY_MULTIPLIER),
		EON(MILLENIA, EON_TO_MILLENIUM_MULTIPLIER);

		private static final class EasyConverter implements TimeUnitConvertible {
			private final TimeUnit unit;
			private final BigDecimal multiplier;

			public EasyConverter(TimeUnit unit, BigDecimal multiplier) {
				this.unit = unit;
				this.multiplier = multiplier;
			}

			public EasyConverter(TimeUnit unit, long multiplier) {
				this(unit, new BigDecimal(multiplier));
			}

			public EasyConverter(TimeUnit unit, double multiplier) {
				this(unit, new BigDecimal(multiplier));
			}

			public BigDecimal toPicoseconds(BigDecimal amount) {
				return unit.toPicoseconds(amount).multiply(multiplier);
			}

			public BigDecimal toNanoseconds(BigDecimal amount) {
				return unit.toNanoseconds(amount).multiply(multiplier);
			}

			public BigDecimal toMicroseconds(BigDecimal amount) {
				return unit.toMicroseconds(amount).multiply(multiplier);
			}

			public BigDecimal toMilliseconds(BigDecimal amount) {
				return unit.toMilliseconds(amount).multiply(multiplier);
			}

			public BigDecimal toSeconds(BigDecimal amount) {
				return unit.toSeconds(amount).multiply(multiplier);
			}

			public BigDecimal toMinutes(BigDecimal amount) {
				return unit.toMinutes(amount).multiply(multiplier);
			}

			public BigDecimal toHours(BigDecimal amount) {
				return unit.toHours(amount).multiply(multiplier);
			}

			public BigDecimal toDays(BigDecimal amount) {
				return unit.toDays(amount).multiply(multiplier);
			}

			public BigDecimal toWeeks(BigDecimal amount) {
				return unit.toWeeks(amount).multiply(multiplier);
			}

			public BigDecimal toMonths(BigDecimal amount) {
				return unit.toMonths(amount).multiply(multiplier);
			}

			public BigDecimal toYears(BigDecimal amount) {
				return unit.toYears(amount).multiply(multiplier);
			}

			public BigDecimal toDecades(BigDecimal amount) {
				return unit.toDecades(amount).multiply(multiplier);
			}

			public BigDecimal toCenturies(BigDecimal amount) {
				return unit.toCenturies(amount).multiply(multiplier);
			}

			public BigDecimal toMillenia(BigDecimal amount) {
				return unit.toMillenia(amount).multiply(multiplier);
			}

			public BigDecimal toEons(BigDecimal amount) {
				return unit.toEons(amount).multiply(multiplier);
			}

		}

		private final TimeUnitConvertible converter;

		private TimeUnit(TimeUnitConvertible converter) {
			this.converter = converter;
		}

		private TimeUnit(TimeUnit other, long multiplier) {
			this(new EasyConverter(other, multiplier));
		}

		private TimeUnit(TimeUnit other, double multiplier) {
			this(new EasyConverter(other, multiplier));
		}

		public BigDecimal toPicoseconds(BigDecimal amount) {
			return converter.toPicoseconds(amount);
		}

		public BigDecimal toNanoseconds(BigDecimal amount) {
			return converter.toNanoseconds(amount);
		}

		public BigDecimal toMicroseconds(BigDecimal amount) {
			return converter.toMicroseconds(amount);
		}

		public BigDecimal toMilliseconds(BigDecimal amount) {
			return converter.toMilliseconds(amount);
		}

		public BigDecimal toSeconds(BigDecimal amount) {
			return converter.toSeconds(amount);
		}

		public BigDecimal toMinutes(BigDecimal amount) {
			return converter.toMinutes(amount);
		}

		public BigDecimal toHours(BigDecimal amount) {
			return converter.toHours(amount);
		}

		public BigDecimal toDays(BigDecimal amount) {
			return converter.toDays(amount);
		}

		public BigDecimal toWeeks(BigDecimal amount) {
			return converter.toWeeks(amount);
		}

		public BigDecimal toMonths(BigDecimal amount) {
			return converter.toMonths(amount);
		}

		public BigDecimal toYears(BigDecimal amount) {
			return converter.toYears(amount);
		}

		public BigDecimal toDecades(BigDecimal amount) {
			return converter.toDecades(amount);
		}

		public BigDecimal toCenturies(BigDecimal amount) {
			return converter.toCenturies(amount);
		}

		public BigDecimal toMillenia(BigDecimal amount) {
			return converter.toMillenia(amount);
		}

		public BigDecimal toEons(BigDecimal amount) {
			return converter.toEons(amount);
		}

	}

	private final BigInteger seconds;
	private final BigDecimal second;

	private Time(BigInteger seconds, BigDecimal second) {
		this.seconds = seconds;
		this.second = second;
	}

}
