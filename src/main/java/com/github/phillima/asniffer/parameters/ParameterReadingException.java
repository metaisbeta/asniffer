package com.github.phillima.asniffer.parameters;

public class ParameterReadingException extends RuntimeException {
	
	public ParameterReadingException() {
	}

	public ParameterReadingException(String message) {
		super(message);
	}

	public ParameterReadingException(Throwable cause) {
		super(cause);
	}

	public ParameterReadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterReadingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	

}
