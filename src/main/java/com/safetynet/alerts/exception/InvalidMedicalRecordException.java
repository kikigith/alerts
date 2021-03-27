package com.safetynet.alerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMedicalRecordException extends RuntimeException {

	public InvalidMedicalRecordException() {
		super();
	}

	public InvalidMedicalRecordException(String message) {
		super(message);
	}

	public InvalidMedicalRecordException(String message, Throwable cause) {
		super(message, cause);
	}
}
