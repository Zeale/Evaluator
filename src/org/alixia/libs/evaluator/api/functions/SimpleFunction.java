package org.alixia.libs.evaluator.api.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.alixia.libs.evaluator.api.terms.Term;

public class SimpleFunction<I, R> {

	private static final List<SimpleFunction<?, ?>> functions = new ArrayList<>(1);

	public static final SimpleFunction<Double, Double> squareRoot = new SimpleFunction<>(Math::sqrt, "sqrt",
			"square_root");

	public final static class Alias {
		private final String name;
		private final boolean ignoreCase;

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Alias))
				return false;
			Alias als = (Alias) obj;
			return ignoreCase || als.ignoreCase ? name.equalsIgnoreCase(als.name) : name.equals(als.name);
		}

		public Alias(String name, boolean ignoreCase) {
			name = name.replaceAll("-", "_").replaceAll(" ", "_");
			while (name.contains("__"))
				name.replaceAll("__", "_");
			this.name = name;
			this.ignoreCase = ignoreCase;
		}

		@Override
		public String toString() {
			return "Alias::" + name;
		}

	}

	public static SimpleFunction<?, ?> getFunction(String name) {
		Alias als = new Alias(name, false);
		for (SimpleFunction<?, ?> sf : functions)
			for (Alias a : sf.aliases)
				if (a.equals(als))
					return sf;
		return null;
	}

	private final List<Alias> aliases = new ArrayList<>();

	private static final <DT> Term<DT> wrap(DT data) {
		return () -> data;
	}

	public SimpleFunction(Function<I, R> function, Alias... aliases) {
		this.function = function;
		wrapperFunction = SimpleFunction::wrap;
		for (Alias a : aliases)
			this.aliases.add(a);
	}

	{
		functions.add(this);
	}

	public SimpleFunction(Function<I, R> function, String... aliases) {
		this.function = function;
		wrapperFunction = SimpleFunction::wrap;
		for (String s : aliases)
			this.aliases.add(new Alias(s, true));
	}

	public SimpleFunction(Function<I, R> function, Function<R, Term<R>> wrapperFunction, Alias... aliases) {
		this.function = function;
		this.wrapperFunction = wrapperFunction;
		for (Alias a : aliases)
			this.aliases.add(a);
	}

	private final Function<I, R> function;
	private final Function<R, Term<R>> wrapperFunction;

	public Term<R> evaluate(I input) {
		return wrapperFunction.apply(function.apply(input));
	}

}
