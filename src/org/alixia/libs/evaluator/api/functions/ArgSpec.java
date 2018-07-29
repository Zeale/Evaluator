package org.alixia.libs.evaluator.api.functions;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(PARAMETER)
public @Documented @interface ArgSpec {
	boolean necessary() default true;

	Class<?> validInputs() default void.class;

	boolean variadic() default false;
}
