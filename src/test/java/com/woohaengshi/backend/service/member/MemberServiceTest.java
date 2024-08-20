package com.woohaengshi.backend.service.member;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.member.ChangePasswordRequest;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.support.fixture.MemberFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void 비밀번호를_변경할_수_있다() {
        Member member = MemberFixture.builder().id(1L).build();
        ChangePasswordRequest request = new ChangePasswordRequest(member.getPassword(), "newPassword12!@");
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(request.getOldPassword(), member.getPassword())).willReturn(true);
        given(passwordEncoder.encode(request.getNewPassword())).willReturn("encodedPassword");
        memberService.changePassword(request, member.getId());
    }
}
