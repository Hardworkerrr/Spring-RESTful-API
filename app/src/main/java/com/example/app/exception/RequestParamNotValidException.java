package com.example.app.exception;

public class RequestParamNotValidException extends RuntimeException {
    public RequestParamNotValidException() {

    }

    public RequestParamNotValidException(String message) {
        super(message);
    }
}
