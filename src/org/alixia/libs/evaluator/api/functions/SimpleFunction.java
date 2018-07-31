package org.alixia.libs.evaluator.api.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.alixia.libs.evaluator.api.terms.Term;

public class SimpleFunction<I, R> {

	public final static class Alias {
		private final String name;
		private final boolean ignoreCase;

		public Alias(String name, final boolean ignoreCase) {
			name = name.replaceAll("-", "_").replaceAll(" ", "_");
			while (name.contains("__"))
				name.replaceAll("__", "_");
			this.name = name;
			this.ignoreCase = ignoreCase;
		}

		@Override
		public boolean equals(final Object obj) {
			if (!(obj instanceof Alias))
				return false;
			final Alias als = (Alias) obj;
			return ignoreCase || als.ignoreCase ? name.equalsIgnoreCase(als.name) : name.equals(als.name);
		}

		@Override
		public String toString() {
			return "Alias::" + name;
		}

	}

	private static final List<SimpleFunction<?, ?>> functions = new ArrayList<>(1);
	public static final SimpleFunction<Double, Double> SQUARE_ROOT = new SimpleFunction<>(Math::sqrt, "sqrt",
			"square_root"), SINE = new SimpleFunction<>(Math::sin, "sin", "sine"),
			COSINE = new SimpleFunction<>(Math::cos, "cos", "cosin", "cosine"),
			TANGENT = new SimpleFunction<>(Math::tan, "tan", "tangent"),
			CUBE_ROOT = new SimpleFunction<>(Math::cbrt, "cbrt", "cube_root"),
			CEIL = new SimpleFunction<>(Math::ceil, "ceil"), FLOOR = new SimpleFunction<>(Math::floor, "floor");

	public static final SimpleFunction<Double, Long> ROUND = new SimpleFunction<>(Math::round, "round");

	public static SimpleFunction<?, ?> getFunction(final String name) {
		final Alias als = new Alias(name, false);
		for (final SimpleFunction<?, ?> sf : functions)
			for (final Alias a : sf.aliases)
				if (a.equals(als))
					return sf;
		return null;
	}

	private static final <DT> Term<DT> wrap(final DT data) {
		return () -> data;
	}

	private final List<Alias> aliases = new ArrayList<>();

	{
		functions.add(this);
	}

	private final Function<I, R> function;

	private final Function<R, Term<R>> wrapperFunction;

	public SimpleFunction(final Function<I, R> function, final Alias... aliases) {
		this.function = function;
		wrapperFunction = SimpleFunction::wrap;
		for (final Alias a : aliases)
			this.aliases.add(a);
	}

	public SimpleFunction(final Function<I, R> function, final Function<R, Term<R>> wrapperFunction,
			final Alias... aliases) {
		this.function = function;
		this.wrapperFunction = wrapperFunction;
		for (final Alias a : aliases)
			this.aliases.add(a);
	}

	public SimpleFunction(final Function<I, R> function, final String... aliases) {
		this.function = function;
		wrapperFunction = SimpleFunction::wrap;
		for (final String s : aliases)
			this.aliases.add(new Alias(s, true));
	}

	public Term<R> evaluate(final I input) {
		return wrapperFunction.apply(function.apply(input));
	}

}
