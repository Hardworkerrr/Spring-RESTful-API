package com.example.app.enums;

public enum RestApiErrorMsg {
    USER_DOESNT_EXISTS_MSG_TEXT("Requested user doesn't exists"),
    USER_UNDER_ALLOWED_AGE_MSG_TEXT("Users under 18 years of age cannot register"),
    USER_DATA_VALIDATION_FAILED_MSG_TEXT("User data validation error"),
    REQUEST_PARAM_NOT_VALID_MSG_TEXT("Invalid request parameter: birthDateFrom, birthDateTo");

    private final String errorMsgText;

    RestApiErrorMsg(String errorMsgText) {
        this.errorMsgText = errorMsgText;
    }

    public String value() {
        return errorMsgText;
    }
}
