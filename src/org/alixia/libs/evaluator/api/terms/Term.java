package org.alixia.libs.evaluator.api.terms;

import org.alixia.libs.evaluator.api.types.Data;

public interface Term<DT extends Data<?>> {
	DT evaluate();

	static <DT extends Data<?>> Term<DT> wrap(DT data) {
		return () -> data;
	}
}
