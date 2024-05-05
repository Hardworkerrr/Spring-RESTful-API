package com.example.app.exception;

public class NoSuchUserExistsException extends RuntimeException{
    public NoSuchUserExistsException(){

    }
    public NoSuchUserExistsException(String message) {
        super(message);
    }
}
