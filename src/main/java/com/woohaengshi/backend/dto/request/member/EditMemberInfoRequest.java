package com.woohaengshi.backend.dto.request.member;

import com.woohaengshi.backend.domain.member.Course;
import com.woohaengshi.backend.domain.member.Member;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class EditMemberInfoRequest {

    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "과정은 필수 입니다.")
    private String course;

    public EditMemberInfoRequest(String name, String course) {
        this.name = name;
        this.course = course;
    }

    private EditMemberInfoRequest() {}

    public Member toMember() {
        return Member.builder().name(name).course(Course.from(course)).build();
    }
}
