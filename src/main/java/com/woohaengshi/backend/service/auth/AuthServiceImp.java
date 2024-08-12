package com.woohaengshi.backend.service.auth;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.studyrecord.auth.SignInRequest;
import com.woohaengshi.backend.dto.response.auth.SignInResponse;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.woohaengshi.backend.exception.ErrorCode.FAIL_TO_SIGN_IN;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public SignInResponse signIn(SignInRequest request) {
        Member member = findMemberByRequest(request);
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        return SignInResponse.of(accessToken, member);
    }

    private Member findMemberByRequest(SignInRequest request) {
        return memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new WoohaengshiException(FAIL_TO_SIGN_IN));
    }
}
