package com.woohaengshi.backend.service.auth;

import com.woohaengshi.backend.dto.request.studyrecord.auth.SignInRequest;
import com.woohaengshi.backend.dto.response.auth.SignInResponse;

public interface AuthService {
    SignInResponse signIn(SignInRequest request);
}
