package com.woohaengshi.backend.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public enum ErrorCode {
    MEMBER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),
    INVALID_INPUT(BAD_REQUEST, "잘못된 입력 값입니다."),
    MISSING_ISSUER_TOKEN(UNAUTHORIZED, "issuer가 존재하지 않는 토큰입니다"),
    NOT_WOOHAENGSHI_TOKEN(UNAUTHORIZED, "발급자가 잘못된 토큰입니다"),
    EXPIRED_TOKEN(UNAUTHORIZED, "토큰이 만료되었습니다"),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "지원하지 않는 토큰 형식입니다"),
    INCORRECTLY_CONSTRUCTED_TOKEN(UNAUTHORIZED, "잘못된 구조의 토큰입니다"),
    FAILED_SIGNATURE_TOKEN(UNAUTHORIZED, "서명에 실패한 토큰입니다"),
    INVALID_CLAIM_TYPE(UNAUTHORIZED, "토큰의 claim값은 Long 타입이어야 합니다"),
    NOT_EXIST_ACCESS_TOKEN(UNAUTHORIZED, "액세스 토큰이 존재하지 않습니다."),
    INCORRECT_CONSTRUCT_HEADER(UNAUTHORIZED, "잘못된 형식의 인증 헤더입니다."),
    SUBJECT_NOT_FOUND(NOT_FOUND, "과목을 찾을 수 없습니다.");

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
