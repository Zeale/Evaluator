package org.alixia.libs.evaluator.api.operators;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.alixia.libs.evaluator.api.terms.ChainTerm;
import org.alixia.libs.evaluator.api.terms.Term;
import org.alixia.libs.evaluator.api.types.Data;
import org.alixia.libs.evaluator.api.types.NumericData;

public interface SimpleOperator<R extends Data<?>> extends Operator {

	public final static SimpleOperator<NumericData> FACTORIAL = inputTerm -> {
		BigInteger result = BigInteger.ONE, input = inputTerm.evaluate().toNumericData().evaluate().toBigInteger();
		for (BigInteger i = BigInteger.ONE; i.compareTo(input) < 1; i = i.add(BigInteger.ONE))
			result = result.multiply(i);
		return Term.wrap(new NumericData(new BigDecimal(result)));
	};

	public Term<R> operate(Term<?> inputTerm);

	@Override
	default void evaluate(ChainTerm<?>.MathChain chain, ChainTerm<?>.MathChain.MathIterator iterator) {
		ChainTerm<?>.MathChain.Pair current = iterator.current();
		current.setFirst(operate(current.getFirst()));
	}
}
