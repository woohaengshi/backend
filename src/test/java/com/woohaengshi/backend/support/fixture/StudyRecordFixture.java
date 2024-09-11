package com.woohaengshi.backend.support.fixture;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;

import java.time.LocalDate;

public class StudyRecordFixture {
    private Long id;
    private int time = 10;
    private LocalDate date = LocalDate.now();
    private Member member;
    private String comment;

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

    public StudyRecordFixture comment(String comment) {
        this.comment = comment;
        return this;
    }

    public StudyRecord build() {
        return StudyRecord.builder().id(id).date(date).member(member).time(time).comment(comment).build();
    }

    public static StudyRecord from(SaveRecordRequest request) {
        return from(request, null);
    }

    public static StudyRecord from(SaveRecordRequest request, Long id) {
        return StudyRecord.builder().id(id).time(request.getTime()).date(request.getDate()).build();
    }
}
