package com.woohaengshi.backend.exception;

public class WoohaengshiException extends RuntimeException{

    private final ErrorCode errorCode;

    public WoohaengshiException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
