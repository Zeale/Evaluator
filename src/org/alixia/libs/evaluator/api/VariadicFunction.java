package org.alixia.libs.evaluator.api;

public interface VariadicFunction<I, R> {
	R execute(@SuppressWarnings("unchecked") I...inputs);
}
