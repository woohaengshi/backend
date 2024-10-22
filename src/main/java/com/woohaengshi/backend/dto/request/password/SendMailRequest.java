package com.woohaengshi.backend.dto.request.password;

import jakarta.validation.constraints.Email;

import lombok.Getter;

@Getter
public class SendMailRequest {

    @Email(message = "잘못된 이메일 형식입니다.")
    private final String email;

    private final String course;

    private final String name;

    public SendMailRequest(String email, String course, String name) {
        this.email = email;
        this.course = course;
        this.name = name;
    }
}
