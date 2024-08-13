package com.woohaengshi.backend.controller.auth;

import static com.woohaengshi.backend.exception.ErrorCode.NOT_EXIST_ACCESS_TOKEN;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.service.auth.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberId.class)
                && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Long resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {
        String authorization = webRequest.getHeader(AUTHORIZATION);
        MemberId memberId = parameter.getParameterAnnotation(MemberId.class);
        if (memberId == null || !memberId.required()) {
            return null;
        }
        validateExistAuthHeader(authorization);
        String accessToken = jwtTokenProvider.extractAccessToken(authorization);
        return jwtTokenProvider.getMemberId(accessToken);
    }

    private void validateExistAuthHeader(String authorization) {
        if (authorization == null) {
            throw new WoohaengshiException(NOT_EXIST_ACCESS_TOKEN);
        }
    }
}
