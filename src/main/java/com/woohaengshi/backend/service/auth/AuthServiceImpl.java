package com.woohaengshi.backend.service.auth;

import static com.woohaengshi.backend.domain.subject.DefaultSubject.getDefaultSubjects;
import static com.woohaengshi.backend.exception.ErrorCode.EMAIL_ALREADY_EXIST;
import static com.woohaengshi.backend.exception.ErrorCode.FAIL_TO_SIGN_IN;
import static com.woohaengshi.backend.exception.ErrorCode.NOT_EXIST_REFRESH_TOKEN;
import static com.woohaengshi.backend.exception.ErrorCode.QUIT_MEMBER;
import static com.woohaengshi.backend.exception.ErrorCode.REFRESH_TOKEN_EXPIRED;
import static com.woohaengshi.backend.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;

import static java.util.Objects.isNull;

import com.woohaengshi.backend.domain.RefreshToken;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.request.auth.SignInRequest;
import com.woohaengshi.backend.dto.request.auth.SignUpRequest;
import com.woohaengshi.backend.dto.result.SignInResult;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.RefreshTokenRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.repository.statistics.StatisticsRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${security.refresh.expiration}")
    private Long expirationSeconds;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatisticsRepository statisticsRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public SignInResult signIn(SignInRequest request) {
        Member member = findMemberByRequest(request);
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        refreshTokenRepository.deleteAllByMemberId(member.getId());
        RefreshToken refreshToken = refreshTokenRepository.save(createRefreshToken(member));
        return SignInResult.of(refreshToken.getToken(), accessToken, member);
    }

    private RefreshToken createRefreshToken(Member member) {
        return RefreshToken.builder().expirationSeconds(expirationSeconds).member(member).build();
    }

    private Member findMemberByRequest(SignInRequest request) {
        Member member = findMemberByEmail(request);
        validateCorrectPassword(request, member);
        validateQuitMember(member);
        return member;
    }

    private void validateQuitMember(Member member) {
        if (!member.isActive()) {
            throw new WoohaengshiException(QUIT_MEMBER);
        }
    }

    private Member findMemberByEmail(SignInRequest request) {
        return memberRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new WoohaengshiException(FAIL_TO_SIGN_IN));
    }

    private void validateCorrectPassword(SignInRequest request, Member member) {
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new WoohaengshiException(FAIL_TO_SIGN_IN);
        }
    }

    @Override
    public SignInResult reissue(String token) {
        validateExistRefreshToken(token);
        RefreshToken refreshToken = findRefreshToken(token);
        validateRefreshTokenExpired(refreshToken);
        refreshToken.reissue(expirationSeconds);
        Member member = refreshToken.getMember();
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        return SignInResult.of(refreshToken.getToken(), accessToken, member);
    }

    @Override
    public void signOut(String refreshToken) {
        if (!isNull(refreshToken)) {
            refreshTokenRepository.delete(findRefreshToken(refreshToken));
        }
    }

    @Override
    public void signUp(SignUpRequest request) {
        validateAlreadyExistEmail(request);
        Member member = request.toMember(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(member);
        getDefaultSubjects(member.getCourse())
                .forEach(subject -> subjectRepository.save(new Subject(subject, member)));
        statisticsRepository.save(new Statistics(member));
    }

    private void validateAlreadyExistEmail(SignUpRequest request) {
        if (memberRepository.existsAllByEmail(request.getEmail())) {
            throw new WoohaengshiException(EMAIL_ALREADY_EXIST);
        }
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
        if (refreshToken == null) {
            throw new WoohaengshiException(NOT_EXIST_REFRESH_TOKEN);
        }
    }
}
