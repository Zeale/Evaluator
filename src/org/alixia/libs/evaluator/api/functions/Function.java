package org.alixia.libs.evaluator.api.functions;

import org.alixia.libs.evaluator.api.VariadicFunction;
import org.alixia.libs.evaluator.api.terms.Number;

public class Function<T, AST> {
	private final String name;
	private final boolean ignoreCase;

	public static final Number<Double> sine(double input) {
		return new Number<Double>(Math.sin(input));
	}


	private final VariadicFunction<AST, T> function;

	public Function(String name, boolean ignoreCase, VariadicFunction<AST, T> function) {
		this.name = name;
		this.ignoreCase = ignoreCase;
		this.function = function;
	}

	public T execute(@SuppressWarnings("unchecked") AST... args) {
		return function.execute(args);
	}

}
