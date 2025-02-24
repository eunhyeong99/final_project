package com.sparta.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String message = "잘못된 요청입니다.";
        ErrorResponse errorResponse = ErrorResponse.of(statusCode, message);

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            log.error("{} - {} : {}", e.getClass().getSimpleName(), fieldError.getField(), fieldError.getDefaultMessage());
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errorResponse;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(final BaseException e) {
        log.error("{} - {}", e.getClass().getSimpleName(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(e.getStatusCode(), e.getMessage());
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }
}