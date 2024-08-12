package com.woohaengshi.backend.controller.auth;

import com.woohaengshi.backend.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshCookieProvider {

    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    public ResponseCookie createRefreshTokenCookie(RefreshToken refreshToken) {
        return createBaseCookie(refreshToken.getToken())
                .maxAge(refreshToken.getExpirationTime())
                .build();
    }

    public ResponseCookie.ResponseCookieBuilder createBaseCookie(String cookieValue) {
        return ResponseCookie.from(REFRESH_TOKEN, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.NONE.attributeValue());
    }

    public ResponseCookie createLogoutCookie() {
        return createBaseCookie("")
                .maxAge(0)
                .build();
    }
}