package com.woohaengshi.backend.domain;

import com.woohaengshi.backend.domain.subject.Subject;
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
public class StudySubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "study_record_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private StudyRecord studyRecord;

    @JoinColumn(name = "subject_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @Builder
    public StudySubject(Long id, StudyRecord studyRecord, Subject subject) {
        this.id = id;
        this.studyRecord = studyRecord;
        this.subject = subject;
    }

    protected StudySubject() {}
}
