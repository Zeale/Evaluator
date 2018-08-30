package org.alixia.libs.evaluator.api.exceptions;

import org.alixia.libs.evaluator.EvaluationException;

public class DeveloperException extends EvaluationException {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public DeveloperException(String message) {
		super(message);
	}

	public DeveloperException(String message, String generalLocation) {
		super(message, generalLocation);
	}

	public DeveloperException(String message, Throwable cause, String generalLocation) {
		super(message, cause, generalLocation);
	}

	public DeveloperException(String message, Throwable cause) {
		super(message, cause);
	}

}
