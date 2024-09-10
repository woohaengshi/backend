package com.woohaengshi.backend.service.password;

import com.woohaengshi.backend.dto.request.password.ChangePasswordRequest;
import com.woohaengshi.backend.dto.request.password.SendMailRequest;

public interface PasswordService {
    void sendMail(SendMailRequest request);
    void changePassword(String code, ChangePasswordRequest request);
    void validateCode(String authenticationCode);
}
