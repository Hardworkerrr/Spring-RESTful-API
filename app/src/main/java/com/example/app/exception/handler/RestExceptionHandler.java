package com.example.app.exception.handler;

import com.example.app.entity.User;
import com.example.app.enums.RestApiError;
import com.example.app.exception.NoSuchUserExistsException;
import com.example.app.exception.RequestParamNotValidException;
import com.example.app.exception.UnderageUserException;
import com.example.app.exception.UserDataValidationFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        Object targetObject = bindingResult.getTarget();
        if (targetObject instanceof User) {
            return this.handleUserDataValidationFailedException(ex, request);
        }
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    @ExceptionHandler({NoSuchUserExistsException.class})
    public ResponseEntity<?> handleNoSuchUserExistsException(Exception ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                RestExceptionHandler.getResponseBody(RestApiError.USER_NOT_FOUND),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler({UnderageUserException.class})
    public ResponseEntity<?> handleUnderageUserException(Exception ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                RestExceptionHandler.getResponseBody(RestApiError.USER_UNDERAGE),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN,
                request
        );
    }


    @ExceptionHandler({UserDataValidationFailedException.class})
    public ResponseEntity<Object> handleUserDataValidationFailedException(MethodArgumentNotValidException ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                RestExceptionHandler.getResponseBody(
                        RestApiError.USER_DATA_VALIDATION_FAILED,
                        ex.getDetailMessageArguments()[1].toString()
                ),
                new HttpHeaders(),
                HttpStatus.UNPROCESSABLE_ENTITY,
                request
        );
    }

    @ExceptionHandler({RequestParamNotValidException.class})
    public ResponseEntity<?> handleRequestParamNotValidException(Exception ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                RestExceptionHandler.getResponseBody(RestApiError.REQUEST_PARAM_VALIDATION_FAILED),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    private static Map<String, Object> getResponseBody(RestApiError error) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("httpCode", error.getHttpStatus().value());
        responseBody.put("errorMsg", error.getErrorMsgText().value());
        responseBody.put("errorCode", error.getErrorCode().value());
        return Map.of("errors", responseBody);
    }

    // Method used for validation requests primarily
    private static Map<String, Object> getResponseBody(RestApiError error, String extendedMsg) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("httpCode", error.getHttpStatus().value());
        responseBody.put("errorMsg", extendedMsg);
        responseBody.put("errorCode", error.getErrorCode().value());
        return Map.of("errors", responseBody);
    }

}