package org.alixia.libs.evaluator;

public abstract class EvaluationException extends RuntimeException {

	/**
	 * SUID
	 */
	private static final long serialVersionUID = 1L;

	public final String generalLocation;

	public EvaluationException(String message, String generalLocation) {
		super(message);
		this.generalLocation = generalLocation;
	}

	public EvaluationException(String message, Throwable cause, String generalLocation) {
		super(message, cause);
		this.generalLocation = generalLocation;
	}

	public EvaluationException(String message, Throwable cause) {
		super(message, cause);
		generalLocation = null;
	}

	public EvaluationException(String message) {
		super(message);
		generalLocation = null;
	}

}
