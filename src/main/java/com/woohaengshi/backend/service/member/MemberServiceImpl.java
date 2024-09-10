package com.woohaengshi.backend.service.member;

import static com.woohaengshi.backend.exception.ErrorCode.QUIT_MEMBER;

import static java.util.Objects.isNull;

import com.woohaengshi.backend.domain.RefreshToken;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.member.ChangePasswordRequest;
import com.woohaengshi.backend.dto.response.member.ShowMemberResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.RefreshTokenRepository;
import com.woohaengshi.backend.s3.AmazonS3Manager;
import com.woohaengshi.backend.s3.Filepath;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Override
    public void changePassword(ChangePasswordRequest request, Long memberId) {
        Member member = findMemberById(memberId);
        validateQuitMember(member);
        validateCorrectPassword(request, member);
        member.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    private void validateQuitMember(Member member) {
        if (!member.isActive()) {
            throw new WoohaengshiException(QUIT_MEMBER);
        }
    }

    private void validateCorrectPassword(ChangePasswordRequest request, Member member) {
        if (!passwordEncoder.matches(request.getOldPassword(), member.getPassword())) {
            throw new WoohaengshiException(ErrorCode.PASSWORD_INCORRECT);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ShowMemberResponse getMemberInfo(Long memberId) {
        Member member = findMemberById(memberId);
        return ShowMemberResponse.from(member);
    }

    @Override
    public void quit(Long memberId, String refreshToken) {
        if (!isNull(refreshToken)) {
            refreshTokenRepository.delete(findRefreshToken(refreshToken));
        }
        Member member = findMemberById(memberId);
        member.quit();
    }

    @Override
    public String changeProfile(Long memberId, MultipartFile multipartFile) {
        Member member = findMemberById(memberId);
        String keyName = amazonS3Manager.makeKeyName(Filepath.PROFILE);
        String filename = amazonS3Manager.uploadFile(keyName, multipartFile);
        member.changeProfile(filename);
        return filename;
    }

    private RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new WoohaengshiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
