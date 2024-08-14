package com.woohaengshi.backend.controller.auth;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.service.auth.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization == null) {
            throw new WoohaengshiException(ErrorCode.NOT_EXIST_ACCESS_TOKEN);
        }
        String accessToken = jwtTokenProvider.extractAccessToken(authorization);
        jwtTokenProvider.validToken(accessToken);
        return true;
    }
}
