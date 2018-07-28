package org.alixia.libs.evaluator.api.functions;

import org.alixia.libs.evaluator.api.VariadicFunction;

public abstract class Function<T, AST> {
	private final String name;
	private final boolean ignoreCase;
	private final Class<? extends AST>[] args;
	private final Class<T> returnType;

	private final VariadicFunction<AST, T> function;

	public Function(String name, boolean ignoreCase, Class<? extends AST>[] args, Class<T> returnType,
			VariadicFunction<AST, T> function) {
		this.name = name;
		this.ignoreCase = ignoreCase;
		this.args = args;
		this.returnType = returnType;
		this.function = function;
	}

	public T execute(@SuppressWarnings("unchecked") AST... args) {
		return function.execute(args);
	}

}
