package com.woohaengshi.backend.service.member;

import static com.woohaengshi.backend.domain.member.State.QUIT;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.woohaengshi.backend.domain.RefreshToken;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.member.ChangePasswordRequest;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.RefreshTokenRepository;
import com.woohaengshi.backend.s3.AmazonS3Manager;
import com.woohaengshi.backend.support.fixture.MemberFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks private MemberServiceImpl memberService;
    @Mock private AmazonS3Manager amazonS3Manager;


    @Test
    void 비밀번호를_변경할_수_있다() {
        Member member = MemberFixture.builder().id(1L).build();
        ChangePasswordRequest request =
                new ChangePasswordRequest(member.getPassword(), "newPassword12!@");
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(request.getOldPassword(), member.getPassword()))
                .willReturn(true);
        given(passwordEncoder.encode(request.getNewPassword())).willReturn("encodedPassword");
        memberService.changePassword(request, member.getId());
        assertThat(member.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void 기존_비밀번호가_일치하지_않을_시_예외() {
        Member member = MemberFixture.builder().id(1L).build();
        ChangePasswordRequest request =
                new ChangePasswordRequest(member.getPassword(), "newPassword12!@");
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(request.getOldPassword(), member.getPassword()))
                .willReturn(false);
        assertThatThrownBy(() -> memberService.changePassword(request, member.getId()))
                .isExactlyInstanceOf(WoohaengshiException.class);
    }

    @Test
    void 회원_존재하지_않을_시_예외() {
        Long NOT_EXIST_MEMBER_ID = 1L;
        ChangePasswordRequest request =
                new ChangePasswordRequest("oldPassword12!@", "newPassword12!@");
        given(memberRepository.findById(NOT_EXIST_MEMBER_ID)).willReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.changePassword(request, NOT_EXIST_MEMBER_ID))
                .isExactlyInstanceOf(WoohaengshiException.class);
    }

    @Test
    void 회원은_탈퇴할_수_있다() {
        Member member = MemberFixture.builder().id(1L).build();
        RefreshToken refreshToken = new RefreshToken(100L, 1L, member);
        given(refreshTokenRepository.findByToken(refreshToken.getToken()))
                .willReturn(Optional.of(refreshToken));
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        memberService.quit(member.getId(), refreshToken.getToken());
        assertThat(member.getState()).isEqualTo(QUIT);
        verify(refreshTokenRepository, times(1)).delete(any(RefreshToken.class));
    }

    @Test
    void 회원_이미지를_변경할_수_있다() throws IOException {
        Member member = MemberFixture.builder().id(1L).build();
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.png",
                "image/png",
                "test image content".getBytes());
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(amazonS3Manager.uploadFile(any(), any())).willReturn("https://image.com/newImage.png");
        assertAll(
                () -> memberService.changeImage(member.getId(), image),
                () -> assertThat(member.getImage()).isEqualTo("https://image.com/newImage.png"),
                () -> verify(amazonS3Manager, times(1)).uploadFile(any(), any())
        );
    }
}
