package com.safetynet.alerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPersonException extends RuntimeException {

	public InvalidPersonException() {
		super();
	}

	public InvalidPersonException(String message) {
		super(message);
	}

	public InvalidPersonException(String message, Throwable cause) {
		super(message, cause);
	}
}
