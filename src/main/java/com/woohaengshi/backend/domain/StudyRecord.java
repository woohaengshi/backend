package com.woohaengshi.backend.domain;

import com.woohaengshi.backend.domain.member.Member;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "studyRecord")
    private List<StudySubject> studySubjects;

    protected StudyRecord() {}

    @Builder
    public StudyRecord(Long id, int time, LocalDate date, Member member) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.member = member;
    }

    public void updateTime(int time) {
        this.time += time;
    }
}
