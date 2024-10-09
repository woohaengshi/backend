package com.woohaengshi.backend.service.member;

import com.woohaengshi.backend.dto.request.member.ChangePasswordRequest;
import com.woohaengshi.backend.dto.response.member.ShowMemberResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    void changePassword(ChangePasswordRequest request, Long memberId);

    ShowMemberResponse getMemberInfo(Long memberId);

    void quit(Long memberId, String refreshToken);

    void changeImage(Long memberId, MultipartFile imageFile);
}
