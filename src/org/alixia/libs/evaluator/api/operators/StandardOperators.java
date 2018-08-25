package org.alixia.libs.evaluator.api.operators;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.alixia.libs.evaluator.Evaluator;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.Data;
import org.alixia.libs.evaluator.api.types.NumericData;

public enum StandardOperators implements NormalOperator, Precedented {
	ADD((BigDecimalHandler) BigDecimal::add, 1), SUBTRACT((BigDecimalHandler) BigDecimal::subtract, 1),
	MULTIPLY((BigDecimalHandler) BigDecimal::multiply, 2), DIVIDE((BigDecimalHandler) Evaluator::divideSafely, 2),
	EXPONENTIATION((BigDecimalHandler) (a, b) -> a.pow(b.intValue()), 3),
	MODULUS((BigDecimalHandler) BigDecimal::remainder, 2);

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
			private final BiFunction<DT, Data<?>, Data<?>> function;

			public Handle(Class<DT> cls, BiFunction<DT, Data<?>, Data<?>> function) {
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
			Class<?> tType = t.getClass();
			List<Handle<?>> possibleHandles = new LinkedList<>();

			for (Handle<?> h : handles)
				if (tType.isAssignableFrom(h.cls)) {
					possibleHandles.add(h);
				}

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
				numberHandler.function.apply(t.toNumericData(), u.toNumericData());

			return ((Handle<Data<?>>) selectedHandle).function.apply(selectedHandle.cls.cast(t), u);
		}

	}

}
