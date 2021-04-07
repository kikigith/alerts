package com.safetynet.alerts.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FirestationNotFoundException extends RuntimeException {

    public FirestationNotFoundException(){
        super();
    }
    public FirestationNotFoundException(String message){
        super(message);
    }

    public FirestationNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}
