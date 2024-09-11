package com.woohaengshi.backend.domain;

import com.woohaengshi.backend.domain.member.Member;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@Entity
@Getter
public class StudyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time", nullable = false)
    private int time;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "comment")
    private String comment;

    protected StudyRecord() {}

    @Builder
    public StudyRecord(Long id, int time, LocalDate date, Member member, String comment) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.member = member;
        this.comment = comment;
    }

    public void updateTime(int time) {
        this.time = time;
    }

    public void updateComment(String comment) {
        if (!isNull(comment)) this.comment = comment;
    }
}
