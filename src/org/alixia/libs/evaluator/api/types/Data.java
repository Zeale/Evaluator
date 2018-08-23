package org.alixia.libs.evaluator.api.types;

public interface Data<T> {

	<DT extends Data<?>> Data<T> cast(DT item);

	NumericData toNumericData();

	/**
	 * Sets the value of this {@link Data} object to be such that it represents the
	 * given {@link NumericData} upon method invocation.
	 * 
	 * @param data The {@link NumericData} that the new value of this {@link Data}
	 *             will be obtained from.
	 */
	void fromNumericData(NumericData data);

	Data<T> clone();

	T evaluate();

}
