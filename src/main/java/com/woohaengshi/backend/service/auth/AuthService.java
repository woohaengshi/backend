package com.woohaengshi.backend.service.auth;

import com.woohaengshi.backend.dto.request.auth.SignInRequest;
import com.woohaengshi.backend.dto.request.auth.SignUpRequest;
import com.woohaengshi.backend.dto.result.SignInResult;

import org.springframework.http.ResponseCookie;

public interface AuthService {
    SignInResult signIn(SignInRequest request);

    SignInResult reissue(String refreshToken);

    void signOut(String refreshToken);

    void signUp(SignUpRequest request);
}
