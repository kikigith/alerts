package com.safetynet.alerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonIntrouvableException extends RuntimeException {
	public PersonIntrouvableException() {
		super();
	}

	public PersonIntrouvableException(String message) {
		super(message);
	}

	public PersonIntrouvableException(String message, Throwable cause) {
		super(message, cause);
	}

}
