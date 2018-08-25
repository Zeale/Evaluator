package org.alixia.libs.evaluator.api.terms;

import org.alixia.libs.evaluator.api.types.Data;

public interface Term<DT extends Data<?>> {
	DT evaluate();

	static <DT extends Data<?>> Term<DT> wrap(DT data) {
		return () -> data;
	}

	static <CT, CDT extends Data<CT>, ODT extends Data<?>> Term<CDT> castWrap(ODT originalData,
			Class<CDT> castDataConstructorGateway) {
		return () -> {
			try {
				return Data.cast(originalData, castDataConstructorGateway);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(
						"The darn devs are back at it again! (A casting error occurred because a data type does not follow specifications.)",
						e);
			}
		};
	}

	static <CT, CDT extends Data<CT>, ODT extends Data<?>> Term<CDT> castTerm(Term<ODT> originalTerm,
			Class<CDT> castDataConstructorGateway) {
		return castWrap(originalTerm.evaluate(), castDataConstructorGateway)::evaluate;
	}

}
