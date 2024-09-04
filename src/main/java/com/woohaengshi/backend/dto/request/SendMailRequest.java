package com.woohaengshi.backend.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class SendMailRequest {

    @Email(message = "잘못된 이메일 형식입니다.")
    private final String email;

    private final Course course;

    private final name;

    private SendMailRequest(){}

    public SendMailRequest(String email) {
        this.email = email;
    }
}
