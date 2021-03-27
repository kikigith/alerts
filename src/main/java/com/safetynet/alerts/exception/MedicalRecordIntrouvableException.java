package com.safetynet.alerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MedicalRecordIntrouvableException extends RuntimeException {
	public MedicalRecordIntrouvableException() {
		super();
	}

	public MedicalRecordIntrouvableException(String message) {
		super(message);
	}

	public MedicalRecordIntrouvableException(String message, Throwable cause) {
		super(message, cause);
	}
}
