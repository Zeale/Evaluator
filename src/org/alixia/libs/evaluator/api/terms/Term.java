package org.alixia.libs.evaluator.api.terms;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.alixia.libs.evaluator.api.types.BooleanData;
import org.alixia.libs.evaluator.api.types.Data;
import org.alixia.libs.evaluator.api.types.NumericData;

public interface Term<DT extends Data<?>> {
	DT evaluate();

	static <DT extends Data<?>> Term<DT> wrap(DT data) {
		return () -> data;
	}

	static <CT, CDT extends Data<CT>, ODT extends Data<?>> Term<CDT> castWrap(ODT originalData,
			Class<CDT> castDataConstructorGateway) {
		return () -> Data.cast(originalData, castDataConstructorGateway);
	}

	static Term<NumericData> factorial(Term<?> inputTerm) {
		BigInteger result = BigInteger.ONE, input = inputTerm.evaluate().toNumericData().evaluate().toBigInteger();
		for (BigInteger i = BigInteger.ONE; i.compareTo(input) < 1; i = i.add(BigInteger.ONE))
			result = result.multiply(i);
		return Term.wrap(new NumericData(new BigDecimal(result)));

	}

	static Term<BooleanData> not(Term<? extends Data<? extends Boolean>> data) {
		return Term.wrap(new BooleanData(!data.evaluate().evaluate()));
	}

	static <CT, CDT extends Data<CT>, ODT extends Data<?>> Term<CDT> castTerm(Term<ODT> originalTerm,
			Class<CDT> castDataConstructorGateway) {
		return castWrap(originalTerm.evaluate(), castDataConstructorGateway)::evaluate;
	}

}
