package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.Subject;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsByNameAndStudyRecordId(String name, Long studyRecordId);

    boolean existsByMemberIdAndName(Long memberId, String name);
}
