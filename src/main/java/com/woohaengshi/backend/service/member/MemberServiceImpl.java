package com.woohaengshi.backend.service.member;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.response.member.ShowMemberResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public ShowMemberResponse getMemberInfo(Long memberId) {
        validateMemberExist(memberId);
        Member member = memberRepository.findById(memberId).get();

        return new ShowMemberResponse(memberId, member.getName(), member.getEmail(), member.getPassword(), member.getImage(), member.getCourse().getName());
    }

    private void validateMemberExist(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}

