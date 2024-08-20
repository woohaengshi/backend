package com.woohaengshi.backend.dto.request.member;

import jakarta.validation.constraints.Pattern;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {

    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
            message = "비밀번호는 영어, 숫자, 특수문자를 포함해 최소 8자 이상 최대 20자 이하만 가능합니다.")
    private String oldPassword;

    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
            message = "비밀번호는 영어, 숫자, 특수문자를 포함해 최소 8자 이상 최대 20자 이하만 가능합니다.")
    private String newPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    private ChangePasswordRequest() {}
}
