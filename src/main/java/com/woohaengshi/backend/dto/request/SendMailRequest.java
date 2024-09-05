package com.woohaengshi.backend.dto.request;

import com.woohaengshi.backend.domain.member.Course;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class SendMailRequest {

    @Email(message = "잘못된 이메일 형식입니다.")
    private final String email;

    private final Course course;

    private final String name;

    public SendMailRequest(String email, Course course, String name) {
        this.email = email;
        this.course = course;
        this.name = name;
    }
}
