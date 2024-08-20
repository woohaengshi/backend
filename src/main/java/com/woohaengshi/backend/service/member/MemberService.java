package com.woohaengshi.backend.service.member;

import com.woohaengshi.backend.dto.request.member.ChangePasswordRequest;

public interface MemberService {

    void changePassword(ChangePasswordRequest request, Long memberId);
}
