package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.StudySubject;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySubjectRepository extends JpaRepository<StudySubject, Long> {
    boolean existsBySubjectIdAndStudyRecordId(Long subjectId, Long studyRecordId);
}
