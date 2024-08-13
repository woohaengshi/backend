package com.woohaengshi.backend.dto.request.studyrecord.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;

import org.hibernate.validator.constraints.Length;

@Getter
public class SignInRequest {
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @Length(min = 8, max = 20)
    @Pattern(regexp = "^[a-zA-Z]+\\d+[!@#$%^&*]+$")
    private String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public SignInRequest() {}
}
