package org.alixia.libs.evaluator.api.types;

/**
 * <b>ALL subclasses must implement the {@link #cast(Data)} method, as well as
 * have a nullary constructor that is publicly accessible to this interface and
 * can be used to create an object at this interface's discretion.</b>
 * 
 * @author Zeale
 *
 * @param <T> The type of object stored in this {@link Data}.
 */
public interface Data<T> {

	/**
	 * <p>
	 * <b>ALL subclasses should implement this method to return a {@link Data}
	 * object that is of the subclass's type.</b> If this is not done,
	 * {@link Data#cast(Data, Class)} won't work properly. All subclasses must also
	 * have a nullary constructor, (i.e., a constructor with no arguments), that is
	 * publicly available for this interface to call upon (reflectively, via a
	 * subclass's {@link Class} object), that will allow the
	 * {@link Data#cast(Data, Class)} method to work properly.
	 * </p>
	 * <p>
	 * Creates a casted {@link Data} of the given {@link Data}. The given data can
	 * be any {@link Data} object, but the casted type <b><i>MUST</i></b> be of the
	 * type of the implementing class.
	 * </p>
	 * <p>
	 * <b>The value of this object, (the one which {@link #cast(Data)} is called
	 * upon), is not modified through this method.</b>
	 * </p>
	 * 
	 * @param item The item being casted.
	 * @return A new object, of the same type as this class, that was casted from
	 *         the specified object.
	 */
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

	String toStringValue();

	static <CT, CDT extends Data<CT>, ODT extends Data<?>> CDT cast(ODT data, Class<CDT> castType) {
		return castUnknown(data, castType);
	}

	@SuppressWarnings("unchecked")
	static <CDT extends Data<?>, ODT extends Data<?>> CDT castUnknown(ODT data, Class<CDT> castType) {
		try {
			return (CDT) castType.newInstance().cast(data);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("The developer has made an error. Please send them the stacktrace. :P", e);

		}
	}

}
