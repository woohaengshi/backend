package com.woohaengshi.backend.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    MEMBER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),
    INVALID_INPUT(BAD_REQUEST, "잘못된 입력 값입니다."),
    SUBJECT_ALREADY_EXISTS(CONFLICT, "이미 존재하는 과목입니다."),
    SUBJECT_NOT_EXISTS(NOT_FOUND, "존재하지 않는 과목입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
