package org.alixia.libs.evaluator.api.functions;

import java.util.function.Function;

import org.alixia.libs.evaluator.api.terms.Term;

public class SimpleFunction<I, R> {

	private static final <DT> Term<DT> wrap(DT data) {
		return () -> data;
	}

	public SimpleFunction(Function<I, R> function) {
		this.function = function;
		wrapperFunction = SimpleFunction::wrap;
	}

	public SimpleFunction(Function<I, R> function, Function<R, Term<R>> wrapperFunction) {
		this.function = function;
		this.wrapperFunction = wrapperFunction;
	}

	private final Function<I, R> function;
	private final Function<R, Term<R>> wrapperFunction;

	public Term<R> evaluate(I input) {
		return wrapperFunction.apply(function.apply(input));
	}

}
