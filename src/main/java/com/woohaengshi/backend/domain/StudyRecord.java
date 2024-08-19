package com.woohaengshi.backend.domain;

import com.woohaengshi.backend.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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

    protected StudyRecord() {}

    @Builder
    public StudyRecord(Long id, int time, LocalDate date, Member member) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.member = member;
    }

    public void updateTime(int time) {
        this.time = time;
    }
}
