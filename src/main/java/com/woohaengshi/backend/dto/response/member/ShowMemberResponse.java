package com.woohaengshi.backend.dto.response.member;

import com.woohaengshi.backend.domain.member.Member;
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

    public static ShowMemberResponse from(Member member) {
        return new ShowMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getImage(), member.getCourse().getName());
    }
}
