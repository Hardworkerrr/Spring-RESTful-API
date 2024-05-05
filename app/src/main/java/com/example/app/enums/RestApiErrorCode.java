package com.example.app.enums;

public enum RestApiErrorCode {

    USER_DOESNT_EXISTS_ERROR_CODE(3001),
    USER_UNDERAGE_ERROR_CODE(3002),
    USER_DATA_VALIDATION_FAILED_ERROR_CODE(3003),
    REQUEST_PARAM_NOT_VALID_ERROR_CODE(3004);

    private final int errorCode;
    RestApiErrorCode(int errorCode) {
        this.errorCode=errorCode;
    }
    public int value() {
        return errorCode;
    }
}
