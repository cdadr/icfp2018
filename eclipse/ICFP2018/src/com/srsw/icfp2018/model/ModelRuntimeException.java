package com.srsw.icfp2018.model;

@SuppressWarnings("serial")
public class ModelRuntimeException extends Exception {

	public ModelRuntimeException() {
	}

	public ModelRuntimeException(String message) {
		super(message);
	}

	public ModelRuntimeException(Throwable cause) {
		super(cause);
	}

	public ModelRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
