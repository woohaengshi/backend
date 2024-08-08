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

@Entity
@Getter
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "study_record_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StudyRecord studyRecord;

    protected Subject() {}

    @Builder
    public Subject(Long id, String name, Member member, StudyRecord studyRecord) {
        this.id = id;
        this.name = name;
        this.member = member;
        this.studyRecord = studyRecord;
    }
}
