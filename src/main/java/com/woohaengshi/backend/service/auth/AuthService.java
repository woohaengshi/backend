package com.woohaengshi.backend.service.auth;

import com.woohaengshi.backend.dto.request.studyrecord.auth.SignInRequest;
import com.woohaengshi.backend.dto.result.SignInResult;

public interface AuthService {
    SignInResult signIn(SignInRequest request);

    SignInResult reissue(String refreshToken);
}
