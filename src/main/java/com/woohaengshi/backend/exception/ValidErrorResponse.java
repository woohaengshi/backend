package com.woohaengshi.backend.exception;

import org.springframework.validation.FieldError;

public class ValidErrorResponse {

    private String field;
    private String message;

    public static ValidErrorResponse from(FieldError error) {
        return new ValidErrorResponse(error.getField(), error.getDefaultMessage());
    }

    public ValidErrorResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
