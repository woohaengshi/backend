package com.woohaengshi.backend.support.fixture;

import com.woohaengshi.backend.domain.member.Course;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.member.State;

public class MemberFixture {

    private Long id;
    private String name = "길가은";
    private String email = "rlfrkdms1@naver.com";
    private String password = "password";
    private Course course = Course.CLOUD_SERVICE;
    private State state = State.ACTIVE;
    private String image = "https://image.com/virtual.png";

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

    public Member build() {
        return Member.builder().id(id).email(email).name(name).image(image).state(state).build();
    }
}
