package com.woohaengshi.backend.service.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.woohaengshi.backend.controller.auth.CookieProvider;
import com.woohaengshi.backend.domain.RefreshToken;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.request.auth.SignInRequest;
import com.woohaengshi.backend.dto.request.auth.SignUpRequest;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.RefreshTokenRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.repository.statistics.StatisticsRepository;
import com.woohaengshi.backend.support.fixture.MemberFixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private CookieProvider cookieProvider;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private SubjectRepository subjectRepository;
    @Mock private StatisticsRepository statisticsRepository;
    @InjectMocks private AuthServiceImpl authService;

    @BeforeEach()
    public void setUp() {
        ReflectionTestUtils.setField(authService, "expirationSeconds", 10000L);
    }

    @Test
    void 로그인을_할_수_있다() {
        SignInRequest signInRequest = new SignInRequest("rlfrkdms1@naver.com", "password12!@");
        Member member = MemberFixture.builder().id(1L).build();
        RefreshToken refreshToken = RefreshToken.builder().expirationSeconds(1000L).build();
        given(memberRepository.findByEmail(signInRequest.getEmail()))
                .willReturn(Optional.of(member));
        given(passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
                .willReturn(true);
        given(jwtTokenProvider.createAccessToken(member.getId())).willReturn("fakeAccessToken");
        given(refreshTokenRepository.save(any(RefreshToken.class))).willReturn(refreshToken);
        assertAll(
                () -> authService.signIn(signInRequest),
                () -> verify(memberRepository, times(1)).findByEmail(signInRequest.getEmail()),
                () -> verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class)));
    }

    @Test
    void 이메일이_일치하지_않으면_예외() {
        SignInRequest signInRequest = new SignInRequest("rlfrkdms1@naver.com", "password12!@");
        given(memberRepository.findByEmail(signInRequest.getEmail())).willReturn(Optional.empty());
        assertAll(
                () ->
                        assertThatThrownBy(() -> authService.signIn(signInRequest))
                                .isExactlyInstanceOf(WoohaengshiException.class),
                () -> verify(memberRepository, times(1)).findByEmail(signInRequest.getEmail()));
    }

    @Test
    void 비밀번호가_일치하지_않으면_예외() {
        SignInRequest signInRequest = new SignInRequest("rlfrkdms1@naver.com", "password12!@");
        Member member = MemberFixture.builder().id(1L).build();
        given(memberRepository.findByEmail(signInRequest.getEmail()))
                .willReturn(Optional.of(member));
        given(passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
                .willReturn(false);
        assertAll(
                () ->
                        assertThatThrownBy(() -> authService.signIn(signInRequest))
                                .isExactlyInstanceOf(WoohaengshiException.class),
                () -> verify(memberRepository, times(1)).findByEmail(signInRequest.getEmail()));
    }

    @Test
    void 회원가입을_할_수_있다() {
        SignUpRequest request =
                new SignUpRequest("강현우", "클라우드 서비스", "rkdgusdn@naver.com", "password12!@");
        given(memberRepository.existsAllByEmail(request.getEmail())).willReturn(false);
        authService.signUp(request);
        verify(subjectRepository, times(3)).save(any(Subject.class));
        verify(statisticsRepository, times(1)).save(any(Statistics.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void 이미_존재하는_이메일이라면_회원가입을_할_수_없다() {
        SignUpRequest request =
                new SignUpRequest("강현우", "클라우드 서비스", "rkdgusdn@naver.com", "password12!@");
        given(memberRepository.existsAllByEmail(request.getEmail())).willReturn(true);
        assertThatThrownBy(() -> authService.signUp(request))
                .isExactlyInstanceOf(WoohaengshiException.class);
        verify(subjectRepository, never()).save(any(Subject.class));
        verify(statisticsRepository, never()).save(any(Statistics.class));
        verify(memberRepository, never()).save(any(Member.class));
    }
}
