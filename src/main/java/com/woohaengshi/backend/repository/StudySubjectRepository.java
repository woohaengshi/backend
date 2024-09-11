package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.StudySubject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface StudySubjectRepository extends JpaRepository<StudySubject, Long> {
    boolean existsBySubjectIdAndStudyRecordId(Long subjectId, Long studyRecordId);
}
