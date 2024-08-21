package com.woohaengshi.backend.dto.result;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.response.auth.SignInResponse;

import lombok.Getter;

@Getter
public class SignInResult {

    private String refreshToken;
    private SignInResponse signInResponse;

    private SignInResult(String refreshToken, SignInResponse signInResponse) {
        this.refreshToken = refreshToken;
        this.signInResponse = signInResponse;
    }

    public static SignInResult of(String refreshToken, String accessToken, Member member) {
        return new SignInResult(refreshToken, SignInResponse.of(accessToken, member));
    }
}
