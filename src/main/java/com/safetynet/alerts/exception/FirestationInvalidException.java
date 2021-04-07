package com.safetynet.alerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FirestationInvalidException extends RuntimeException {
    public FirestationInvalidException(){
        super();
    }
    public FirestationInvalidException(String message) {
        super(message);
    }
    public FirestationInvalidException(String message, Throwable cause){
        super(message,cause);
    }
}
