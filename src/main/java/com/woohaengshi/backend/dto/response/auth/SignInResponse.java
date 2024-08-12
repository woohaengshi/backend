package com.woohaengshi.backend.dto.response.auth;

import com.woohaengshi.backend.domain.member.Member;
import lombok.Getter;

@Getter
public class SignInResponse {

    private String name;
    private String image;
    private String accessToken;

    public SignInResponse(String accessToken, String image, String name) {
        this.accessToken = accessToken;
        this.image = image;
        this.name = name;
    }

    public static SignInResponse of(String accessToken, Member member) {
        return new SignInResponse(accessToken, member.getImage(), member.getName());
    }
}
