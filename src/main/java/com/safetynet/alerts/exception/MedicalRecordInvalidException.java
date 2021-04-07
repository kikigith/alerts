package com.safetynet.alerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MedicalRecordInvalidException extends RuntimeException {

	public MedicalRecordInvalidException() {
		super();
	}

	public MedicalRecordInvalidException(String message) {
		super(message);
	}

	public MedicalRecordInvalidException(String message, Throwable cause) {
		super(message, cause);
	}
}
