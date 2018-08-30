package org.alixia.libs.evaluator.api.exceptions;

public class UnresolvableTypeException extends CastException {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public UnresolvableTypeException(String message, String generalLocation) {
		super(message, generalLocation);
	}

	public UnresolvableTypeException(String message, CharSequence ref) {
		this("A type could not be discerned off of the given reference: " + ref + ". Reason: " + message
				+ (message.endsWith(".") ? "" : "."));
	}

	public UnresolvableTypeException(String message, Throwable cause, String generalLocation) {
		super(message, cause, generalLocation);
	}

	public UnresolvableTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnresolvableTypeException(String message) {
		super(message);
	}

}
