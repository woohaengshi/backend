package com.woohaengshi.backend.controller.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    public static final String REFRESH_TOKEN = "refresh_token";

    @Value("${security.refresh.expiration}")
    private Long expirationSeconds;

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return createBaseRefreshTokenCookie(refreshToken).maxAge(expirationSeconds).build();
    }

    private ResponseCookie.ResponseCookieBuilder createBaseRefreshTokenCookie(String cookieValue) {
        return ResponseCookie.from(REFRESH_TOKEN, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.NONE.attributeValue());
    }

    public ResponseCookie createSignOutCookie() {
        return createBaseRefreshTokenCookie("").maxAge(0).build();
    }
}
