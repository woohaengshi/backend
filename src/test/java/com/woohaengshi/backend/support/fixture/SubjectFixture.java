package com.woohaengshi.backend.support.fixture;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.subject.Subject;

public class SubjectFixture {
    private Long id;
    private String name = "HTML";
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
        return Subject.builder().id(id).name(name).member(member).build();
    }
}
