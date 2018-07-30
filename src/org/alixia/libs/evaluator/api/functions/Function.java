package org.alixia.libs.evaluator.api.functions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.alixia.libs.evaluator.api.VariadicFunction;
import org.alixia.libs.evaluator.api.terms.Number;

public class Function<T, AST> {
	private final String name;
	private final boolean ignoreCase;

	public static void registerClass(Class<?> cls) {
		for (Method m : cls.getDeclaredMethods())
			if (Modifier.isStatic(m.getModifiers()) && m.isAnnotationPresent(FuncSpec.class)) {
				addFunction(m);
			}
	}

	private static void addFunction(Method function) {

	}

	@FuncSpec
	public static final Number<Double> sine(double input) {
		return new Number<Double>(Math.sin(input));
	}

	@FuncSpec
	public static final double add(double first, @ArgSpec(necessary = false) Argument<Double> second) {
		return first + (second == null ? 1 : second.getValue());
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
