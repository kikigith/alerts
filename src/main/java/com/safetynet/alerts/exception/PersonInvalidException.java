package com.safetynet.alerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonInvalidException extends RuntimeException {

	public PersonInvalidException() {
		super();
	}

	public PersonInvalidException(String message) {
		super(message);
	}

	public PersonInvalidException(String message, Throwable cause) {
		super(message, cause);
	}
}
