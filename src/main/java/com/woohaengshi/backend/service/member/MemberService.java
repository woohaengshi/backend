package com.woohaengshi.backend.service.member;

import com.woohaengshi.backend.dto.request.member.ChangePasswordRequest;
import com.woohaengshi.backend.dto.request.member.MemberRequest;
import com.woohaengshi.backend.dto.response.member.ShowMemberResponse;

public interface MemberService {

    void changePassword(ChangePasswordRequest request, Long memberId);

    ShowMemberResponse getMemberInfo(Long memberId);

    void editMemberInfo(MemberRequest memberRequest, Long memberId);

    void quit(Long memberId, String refreshToken);
}
