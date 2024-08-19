package com.woohaengshi.backend.service.member;

import com.woohaengshi.backend.dto.response.member.ShowMemberResponse;

public interface MemberService {
    ShowMemberResponse getMemberInfo(Long memberId);
}
