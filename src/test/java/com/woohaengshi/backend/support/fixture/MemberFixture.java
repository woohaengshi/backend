package com.woohaengshi.backend.support.fixture;

import com.woohaengshi.backend.domain.member.Course;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.member.State;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberFixture {

    private Long id;
    private String name = "길가은";
    private String email = "rlfrkdms1@naver.com";
    private String password = "password12!@";
    private Course course = Course.CLOUD_SERVICE;
    private State state = State.ACTIVE;
    private String image = "https://image.com/virtual.png";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDate sleepDate = LocalDate.now();

    public static MemberFixture builder() {
        return new MemberFixture();
    }

    public MemberFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MemberFixture name(String name) {
        this.name = name;
        return this;
    }

    public MemberFixture email(String email) {
        this.email = email;
        return this;
    }

    public MemberFixture password(String password) {
        this.password = password;
        return this;
    }

    public MemberFixture course(Course course) {
        this.course = course;
        return this;
    }

    public MemberFixture state(State state) {
        this.state = state;
        return this;
    }

    public MemberFixture image(String image) {
        this.image = image;
        return this;
    }

    public MemberFixture createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public MemberFixture sleepDate(LocalDate sleepDate) {
        this.sleepDate = sleepDate;
        return this;
    }

    public Member build() {
        return Member.builder()
                .id(id)
                .email(email)
                .name(name)
                .image(image)
                .state(state)
                .course(course)
                .password(password)
                .createdAt(createdAt)
                .sleepDate(sleepDate)
                .build();
    }
}
