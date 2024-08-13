package com.woohaengshi.backend.exception;

import static com.woohaengshi.backend.exception.ErrorCode.INVALID_INPUT;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleNotSupportedHttpMethodException(
            HttpRequestMethodNotSupportedException exception) {
        String supportedMethods = String.join(", ", exception.getSupportedMethods());
        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(
                        new ErrorResponse(
                                METHOD_NOT_ALLOWED.value(),
                                String.format(
                                        "요청 HTTP METHOD는 <%s>이지만, 해당 URI를 지원하는 HTTP METHOD는"
                                            + " <%s>입니다.",
                                        exception.getMethod(), supportedMethods)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        return ResponseEntity.status(INVALID_INPUT.getStatus())
                .body(
                        new ErrorResponse(
                                INVALID_INPUT.getStatus().value(),
                                String.format(
                                        "잘못된 입력 형식입니다. 에러 메세지 => %s", exception.getMessage())));
    }
}
