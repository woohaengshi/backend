package com.woohaengshi.backend.dto.result;

import com.woohaengshi.backend.dto.response.auth.SignInResponse;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
public class SignInResult {

    private ResponseCookie refreshTokenCookie;
    private SignInResponse signInResponse;

    public SignInResult(ResponseCookie refreshTokenCookie, SignInResponse signInResponse) {
        this.refreshTokenCookie = refreshTokenCookie;
        this.signInResponse = signInResponse;
    }
}
