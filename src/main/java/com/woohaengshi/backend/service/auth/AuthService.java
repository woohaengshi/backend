package com.woohaengshi.backend.service.auth;

import com.woohaengshi.backend.dto.request.studyrecord.auth.SignInRequest;
import com.woohaengshi.backend.dto.result.SignInResult;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    SignInResult signIn(SignInRequest request);

    SignInResult reissue(String refreshToken);

    ResponseCookie signOut(String token);
}
