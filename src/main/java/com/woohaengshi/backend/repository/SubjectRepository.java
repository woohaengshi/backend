package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.Subject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsByNameAndStudyRecordId(String name, Long studyRecordId);

    Stream<Subject> findAllByMemberIdOrderByNameAsc(Long memberId);
}
