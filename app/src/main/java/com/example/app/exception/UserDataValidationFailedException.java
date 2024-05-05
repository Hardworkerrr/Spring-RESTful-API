package com.example.app.exception;

public class UserDataValidationFailedException extends RuntimeException {
    public UserDataValidationFailedException() {

    }

    public UserDataValidationFailedException(String message) {
        super(message);
    }
}
