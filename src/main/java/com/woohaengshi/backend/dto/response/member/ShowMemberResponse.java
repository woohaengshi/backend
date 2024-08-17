package com.woohaengshi.backend.dto.response.member;

import lombok.Getter;

@Getter
public class ShowMemberResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String image;
    private String course;

    public ShowMemberResponse(Long id, String name, String email, String password, String image, String course) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.course = course;
    }
}
