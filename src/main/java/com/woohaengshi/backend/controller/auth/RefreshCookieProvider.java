package com.woohaengshi.backend.controller.auth;

import com.woohaengshi.backend.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshCookieProvider {

    public static final String REFRESH_TOKEN = "refresh_token";

    @Value("${security.refresh.expiration}")
    private Long expirationSeconds;

    public ResponseCookie createRefreshTokenCookie(RefreshToken refreshToken) {
        return createBaseCookie(refreshToken.getToken())
                .maxAge(expirationSeconds)
                .build();
    }

    public ResponseCookie.ResponseCookieBuilder createBaseCookie(String cookieValue) {
        return ResponseCookie.from(REFRESH_TOKEN, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.NONE.attributeValue());
    }

    public ResponseCookie createSignOutCookie() {
        return createBaseCookie("")
                .maxAge(0)
                .build();
    }
}