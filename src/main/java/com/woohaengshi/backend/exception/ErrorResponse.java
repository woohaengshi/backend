package com.woohaengshi.backend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class ErrorResponse {

    private int status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidErrorResponse> errors;

    @Builder
    private ErrorResponse(int status, String message, List<ValidErrorResponse> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse of(List<FieldError> errors, ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .errors(errors.stream().map(ValidErrorResponse::from).toList())
                .build();
    }
}
