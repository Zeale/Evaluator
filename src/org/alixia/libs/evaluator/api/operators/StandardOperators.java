package org.alixia.libs.evaluator.api.operators;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.alixia.libs.evaluator.Evaluator;
import org.alixia.libs.evaluator.api.operators.StandardOperators.OperatorFunction.Handle;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.BooleanData;
import org.alixia.libs.evaluator.api.types.BooleanData.BiBoolFunction;
import org.alixia.libs.evaluator.api.types.Data;
import org.alixia.libs.evaluator.api.types.NumericData;
import org.alixia.libs.evaluator.api.types.ProbabilityData;
import org.alixia.libs.evaluator.api.types.StringData;
import org.alixia.libs.evaluator.api.types.TimeData;

public enum StandardOperators implements NormalOperator, Precedented {
	ADD(new OperatorFunction(new Handle<>(NumericData.class, (BigDecimalHandler) BigDecimal::add),
			new Handle<>(TimeData.class, (t, u) -> {
				BigDecimal value = u.toNumericData().evaluate();
				return new TimeData(t.evaluate().plusSeconds(getFront(value).longValue()).plusNanos(getBack(value)));
			}), new Handle<>(StringData.class, (t, u) -> new StringData(t.evaluate() + u.evaluate())),
			new Handle<ProbabilityData>(ProbabilityData.class,
					(t, u) -> new ProbabilityData(t.evaluate().add(u.toNumericData().evaluate())))),
			1),
	SUBTRACT(new OperatorFunction(new Handle<>(NumericData.class, (BigDecimalHandler) BigDecimal::subtract),
			new Handle<>(TimeData.class, (t, u) -> {
				BigDecimal value = u.toNumericData().evaluate();
				return new TimeData(t.evaluate().plusSeconds(-getFront(value).longValue()).plusNanos(-getBack(value)));
			}),
			new Handle<ProbabilityData>(ProbabilityData.class,
					(t, u) -> new ProbabilityData(t.evaluate().subtract(u.toNumericData().evaluate())))),
			1),
	MULTIPLY(new OperatorFunction(new Handle<>(NumericData.class, (BigDecimalHandler) BigDecimal::multiply),
			new Handle<>(ProbabilityData.class,
					(t, u) -> new ProbabilityData(t.evaluate().multiply(u.toNumericData().evaluate())))),
			2),
	DIVIDE(new OperatorFunction(new Handle<>(NumericData.class, (BigDecimalHandler) Evaluator::divideSafely),
			new Handle<>(ProbabilityData.class, (t, u) -> {
				return new ProbabilityData(Evaluator.divideSafely(t.evaluate(), u.toNumericData().evaluate()));
			})), 2),
	EXPONENTIATION((t, u) -> t instanceof BooleanData ? ((BooleanData) t).xor(Data.cast(u, BooleanData.class))
			: new NumericData(t.toNumericData().evaluate().pow(u.toNumericData().evaluate().intValue())), 3),
	MODULUS((BigDecimalHandler) BigDecimal::remainder, 2),
	AND(new OperatorFunction(new Handle<>(NumericData.class, (BiBoolFunction) BooleanData::and),
			new Handle<>(ProbabilityData.class, (t, u) -> {
				BigDecimal value = t.evaluate().multiply(u.toNumericData().evaluate());
				return u instanceof ProbabilityData ? new ProbabilityData(value) : new NumericData(value);
			})), 5),
	OR(new OperatorFunction(new Handle<>(NumericData.class, (BiBoolFunction) BooleanData::or),
			new Handle<>(ProbabilityData.class, (t, u) -> {
				BigDecimal value = t.evaluate().add(u.toNumericData().evaluate());
				return u instanceof ProbabilityData ? new ProbabilityData(value) : new NumericData(value);
			})), 4);

	public static BigInteger getFront(BigDecimal number) {
		return number.toBigInteger();
	}

	public static long getBack(BigDecimal number) {
		number = Evaluator.roundBigDecimal(number);// Strip trailing zeros.
		return number.remainder(BigDecimal.ONE).movePointRight(number.scale()).abs().toBigInteger().longValue();
	}

	private final BiFunction<Data<?>, Data<?>, Data<?>> function;

	private final Precedence precedence;

	private StandardOperators(final BiFunction<Data<?>, Data<?>, Data<?>> function, final int precedence) {
		this.function = function;
		this.precedence = new Precedence(precedence);
	}

	@Override
	public Term<?> evaluate(final Term<? extends Data<?>> first, final Term<? extends Data<?>> second) {
		return Term.wrap(function.apply(first.evaluate(), second.evaluate()));
	}

	@Override
	public Precedence precedence() {
		return precedence;
	}

	private interface BigDecimalHandler extends BiFunction<Data<?>, Data<?>, Data<?>> {
		@Override
		default Data<?> apply(Data<?> t, Data<?> u) {
			return new NumericData(apply(t.toNumericData().evaluate(), u.toNumericData().evaluate()));
		}

		BigDecimal apply(BigDecimal first, BigDecimal second);
	}

	public static final class OperatorFunction implements BiFunction<Data<?>, Data<?>, Data<?>> {

		private final List<Handle<?>> handles = new LinkedList<>();
		private final Handle<NumericData> numberHandler;

		public OperatorFunction(Handle<NumericData> numberHandler, Handle<?>... others) {
			this.numberHandler = numberHandler;
			for (Handle<?> h : others) {
				if (handles.contains(h) || h.cls == numberHandler.cls)
					throw new RuntimeException("This operator already contains a handle for the type, " + h.cls
							+ ". Violating handle: " + h + ".");
				handles.add(h);
			}
		}

		public static final class Handle<DT extends Data<?>> {
			private final Class<DT> cls;
			private final BiFunction<? super DT, Data<?>, Data<?>> function;

			public Handle(Class<DT> cls, BiFunction<? super DT, Data<?>, Data<?>> function) {
				this.cls = cls;
				this.function = function;
			}

			@Override
			public boolean equals(Object obj) {
				return obj instanceof Handle && ((Handle<?>) obj).cls == cls;
			}

		}

		@SuppressWarnings("unchecked")
		@Override
		public Data<?> apply(Data<?> t, Data<?> u) {
			Class<? extends Data<?>> tType = (Class<? extends Data<?>>) t.getClass();
			List<Handle<?>> possibleHandles = new LinkedList<>();

			for (Handle<?> h : handles)
				if (h.cls.isAssignableFrom(tType))
					possibleHandles.add(h);

			Handle<?> selectedHandle = numberHandler;

			if (possibleHandles.size() == 1)
				selectedHandle = possibleHandles.get(0);
			else
				OUTER: for (Handle<?> h0 : possibleHandles) {
					for (Handle<?> h1 : possibleHandles) {
						if (h0 == h1)
							continue;
						if (h0.cls.isAssignableFrom(h1.cls))
							continue OUTER;
					}
					selectedHandle = h0;
					break;
				}

			if (selectedHandle == numberHandler)
				return numberHandler.function.apply(t.toNumericData(), u.toNumericData());

			return ((Handle<Data<?>>) selectedHandle).function.apply(Data.castUnknown(t, selectedHandle.cls), u);
		}

	}

}
