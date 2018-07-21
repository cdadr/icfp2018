package com.srsw.icfp2018.model;

@SuppressWarnings("serial")
public class TraceFileException extends Exception {

	public TraceFileException() {
	}

	public TraceFileException(String message) {
		super(message);
	}

	public TraceFileException(Throwable cause) {
		super(cause);
	}

	public TraceFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public TraceFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
