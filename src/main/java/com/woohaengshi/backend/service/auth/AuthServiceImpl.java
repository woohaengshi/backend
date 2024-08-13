package com.woohaengshi.backend.service.auth;

import com.woohaengshi.backend.controller.auth.RefreshCookieProvider;
import com.woohaengshi.backend.domain.RefreshToken;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.studyrecord.auth.SignInRequest;
import com.woohaengshi.backend.dto.response.auth.SignInResponse;
import com.woohaengshi.backend.dto.result.SignInResult;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.woohaengshi.backend.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${security.refresh.expiration}")
    private Long expirationSeconds;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshCookieProvider refreshCookieProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public SignInResult signIn(SignInRequest request) {
        Member member = findMemberByRequest(request);
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        SignInResponse signInResponse = SignInResponse.of(accessToken, member);
        RefreshToken refreshToken = refreshTokenRepository.save(createRefreshToken(member));
        ResponseCookie refreshTokenCookie = refreshCookieProvider.createRefreshTokenCookie(refreshToken);
        return new SignInResult(refreshTokenCookie, signInResponse);
    }

    private RefreshToken createRefreshToken(Member member) {
        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expirationSeconds(expirationSeconds)
                .member(member)
                .build();
    }

    private Member findMemberByRequest(SignInRequest request) {
        return memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new WoohaengshiException(FAIL_TO_SIGN_IN));
    }

    @Override
    public SignInResult reissue(String token) {
        validateExistRefreshToken(token);
        RefreshToken refreshToken = findRefreshToken(token);
        validateRefreshTokenExpired(refreshToken);
        refreshToken.reissue(expirationSeconds);
        Member member = refreshToken.getMember();
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        SignInResponse signInResponse = SignInResponse.of(accessToken, member);
        ResponseCookie refreshTokenCookie = refreshCookieProvider.createRefreshTokenCookie(refreshToken);
        return new SignInResult(refreshTokenCookie, signInResponse);
    }

    private void validateRefreshTokenExpired(RefreshToken refreshToken) {
        if (refreshToken.isExpired()) throw new WoohaengshiException(REFRESH_TOKEN_EXPIRED);
    }

    private RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new WoohaengshiException(REFRESH_TOKEN_NOT_FOUND));
    }

    private void validateExistRefreshToken(String refreshToken) {
        if(refreshToken == null){
            throw new WoohaengshiException(NOT_EXIST_REFRESH_TOKEN);
        }
    }
}
