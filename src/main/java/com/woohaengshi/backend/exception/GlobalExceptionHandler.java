package com.woohaengshi.backend.exception;

import static com.woohaengshi.backend.exception.ErrorCode.INVALID_INPUT;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.from(errorCode));
    }

    private static final String METHOD_NOT_SUPPORTED_FORMAT =
            "요청 HTTP METHOD는 <%s>이지만, 해당 URI를 지원하는 HTTP METHOD는 <%s>입니다.";

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleNotSupportedHttpMethodException(
            HttpRequestMethodNotSupportedException exception) {
        String supportedMethods = String.join(", ", exception.getSupportedMethods());
        return ResponseEntity.status(METHOD_NOT_ALLOWED)
                .body(
                        new ErrorResponse(
                                METHOD_NOT_ALLOWED.value(),
                                String.format(
                                        METHOD_NOT_SUPPORTED_FORMAT,
                                        exception.getMethod(),
                                        supportedMethods)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {
        return ResponseEntity.status(INVALID_INPUT.getStatus())
                .body(
                        new ErrorResponse(
                                INVALID_INPUT.getStatus().value(),
                                String.format(
                                        "잘못된 입력 형식입니다. 에러 메세지 => %s", exception.getMessage())));
    }
}
