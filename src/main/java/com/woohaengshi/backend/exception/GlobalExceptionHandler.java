package com.woohaengshi.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.woohaengshi.backend.exception.ErrorCode.INVALID_INPUT;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WoohaengshiException.class)
    public ResponseEntity<ErrorResponse> handleWoohaengshiException(
            WoohaengshiException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(
            MethodArgumentNotValidException exception) {
        return ResponseEntity.status(INVALID_INPUT.getStatus())
                .body(
                        ErrorResponse.of(
                                exception.getBindingResult().getFieldErrors(), INVALID_INPUT));
    }
}
