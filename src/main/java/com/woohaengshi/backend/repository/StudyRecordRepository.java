package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.StudyRecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {
    Optional<StudyRecord> findByDateAndMemberId(LocalDate date, Long memberId);
}
