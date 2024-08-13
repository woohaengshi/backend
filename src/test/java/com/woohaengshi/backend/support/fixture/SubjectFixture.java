package com.woohaengshi.backend.support.fixture;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.domain.member.Member;

public class SubjectFixture {
    private Long id;
    private String name;
    private Member member;
    private StudyRecord studyRecord;

    public static SubjectFixture builder() {
        return new SubjectFixture();
    }

    public SubjectFixture id(Long id) {
        this.id = id;
        return this;
    }

    public SubjectFixture name(String name) {
        this.name = name;
        return this;
    }

    public SubjectFixture member(Member member) {
        this.member = member;
        return this;
    }

    public SubjectFixture studyRecord(StudyRecord studyRecord) {
        this.studyRecord = studyRecord;
        return this;
    }

    public Subject build() {
        return Subject.builder().id(id).member(member).build();
    }
}
