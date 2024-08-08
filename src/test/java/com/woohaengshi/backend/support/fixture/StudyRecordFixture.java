package com.woohaengshi.backend.support.fixture;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;

import java.time.LocalDate;

public class StudyRecordFixture {
    private Long id;
    private int time = 10;
    private LocalDate date = LocalDate.now();
    private Member member;

    public static StudyRecordFixture builder() {
        return new StudyRecordFixture();
    }

    public StudyRecordFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StudyRecordFixture time(int time) {
        this.time = time;
        return this;
    }

    public StudyRecordFixture date(LocalDate date) {
        this.date = date;
        return this;
    }

    public StudyRecordFixture member(Member member) {
        this.member = member;
        return this;
    }

    public StudyRecord build() {
        return StudyRecord.builder().id(id).date(date).member(member).time(time).build();
    }
}
