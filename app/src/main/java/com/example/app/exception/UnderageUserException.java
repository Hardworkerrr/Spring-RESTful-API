package com.example.app.exception;

public class UnderageUserException extends RuntimeException {
    public UnderageUserException() {

    }

    public UnderageUserException(String message) {
        super(message);
    }
}
