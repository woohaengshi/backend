package com.woohaengshi.backend.service.member;

import static com.woohaengshi.backend.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.woohaengshi.backend.exception.ErrorCode.PASSWORD_INCORRECT;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.member.ChangePasswordRequest;
import com.woohaengshi.backend.dto.response.member.ShowMemberResponse;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordRequest request, Long memberId) {
        Member member = findMemberById(memberId);
        validateCorrectPassword(request, member);
        member.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    private void validateCorrectPassword(ChangePasswordRequest request, Member member) {
        if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new WoohaengshiException(PASSWORD_INCORRECT);
        }

    @Override
    @Transactional(readOnly = true)
    public ShowMemberResponse getMemberInfo(Long memberId) {
        Member member = findMemberById(memberId);
        return ShowMemberResponse.from(member);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new WoohaengshiException(MEMBER_NOT_FOUND));
    }
}
