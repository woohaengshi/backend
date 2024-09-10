package com.woohaengshi.backend.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),
    STATISTICS_TYPE_NOT_FOUND(BAD_REQUEST, "랭킹 조회에서 찾을 수 없는 유형 타입입니다."),
    STATISTICS_NOT_FOUND(NOT_FOUND, "통계를 찾을 수 없습니다"),
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
    SUBJECT_NOT_FOUND(NOT_FOUND, "과목을 찾을 수 없습니다."),
    FAIL_TO_SIGN_IN(BAD_REQUEST, "로그인에 실패했습니다."),
    NOT_EXIST_REFRESH_TOKEN(UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED, "리프레시 토큰의 유효기간이 만료되었습니다."),
    SUBJECT_ALREADY_EXISTS(CONFLICT, "이미 존재하는 과목입니다."),
    COURSE_NOT_FOUND(NOT_FOUND, "과정명을 찾을 수 없습니다. "),
    TIME_HAVE_TO_GREATER_THAN_EXIST(BAD_REQUEST, "요청한 공부 기록은 이전 공부 기록 시간 보다 커야 합니다."),
    EMAIL_ALREADY_EXIST(CONFLICT, "이미 존재하는 이메일입니다."),
    FAILED_SAVE_IMAGE(INTERNAL_SERVER_ERROR, "이미지 저장 실패"),
    QUIT_MEMBER(FORBIDDEN, "탈퇴한 회원입니다."),
    INACTIVE_SUBJECT(BAD_REQUEST, "이미 비활성화된 과목입니다."),
    PASSWORD_INCORRECT(BAD_REQUEST, "맞지 않는 비밀번호입니다."),
    CREATE_MAIL_EXCEPTION(INTERNAL_SERVER_ERROR, "메일 생성 중 오류가 발생했습니다."),
    INCORRECT_MEMBER_INFO(BAD_REQUEST, "사용자의 정보가 잘못되었습니다."),
    AUTHENTICATE_CODE_NOT_FOUND(NOT_FOUND, "인증 코드를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
