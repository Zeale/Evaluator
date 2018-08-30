package org.alixia.libs.evaluator.api.exceptions;

import org.alixia.libs.evaluator.EvaluationException;

public class CastException extends EvaluationException {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public CastException(String message, String generalLocation) {
		super(message, generalLocation);
	}

	public CastException(String message, Throwable cause, String generalLocation) {
		super(message, cause, generalLocation);
	}

	public CastException(String message, Throwable cause) {
		super(message, cause);
	}

	public CastException(String message) {
		super(message);
	}

}
