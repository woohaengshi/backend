package com.woohaengshi.backend.dto.response.member;

import lombok.Getter;

@Getter
public class ShowMemberResponse {
    private Long id;
    private String name;
    private String email;
    private String image;
    private String course;

    private ShowMemberResponse(Long id, String name, String email, String image, String course) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.course = course;
    }

    public static ShowMemberResponse of(
            Long id, String name, String email, String image, String course) {
        return new ShowMemberResponse(id, name, email, image, course);
    }
}
