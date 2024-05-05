package com.example.app.enums;

import org.springframework.http.HttpStatus;

public enum RestApiError {
    USER_NOT_FOUND(
            RestApiErrorMsg.USER_DOESNT_EXISTS_MSG_TEXT,
            HttpStatus.NOT_FOUND,
            RestApiErrorCode.USER_DOESNT_EXISTS_ERROR_CODE
    ),
    USER_UNDERAGE(
            RestApiErrorMsg.USER_UNDER_ALLOWED_AGE_MSG_TEXT,
            HttpStatus.FORBIDDEN,
            RestApiErrorCode.USER_UNDERAGE_ERROR_CODE
    ),
    USER_DATA_VALIDATION_FAILED(
            RestApiErrorMsg.USER_DATA_VALIDATION_FAILED_MSG_TEXT,
            HttpStatus.UNPROCESSABLE_ENTITY,
            RestApiErrorCode.USER_DATA_VALIDATION_FAILED_ERROR_CODE
    ),
    REQUEST_PARAM_VALIDATION_FAILED(
            RestApiErrorMsg.REQUEST_PARAM_NOT_VALID_MSG_TEXT,
            HttpStatus.BAD_REQUEST,
            RestApiErrorCode.REQUEST_PARAM_NOT_VALID_ERROR_CODE
    );

    private final RestApiErrorMsg errorMsgText;
    private final HttpStatus httpStatus;
    private final RestApiErrorCode errorCode;

    RestApiError(RestApiErrorMsg errorMsgText, HttpStatus httpStatus, RestApiErrorCode errorCode) {
        this.errorMsgText = errorMsgText;
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public RestApiErrorMsg getErrorMsgText() {
        return errorMsgText;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public RestApiErrorCode getErrorCode() {
        return errorCode;
    }
}
