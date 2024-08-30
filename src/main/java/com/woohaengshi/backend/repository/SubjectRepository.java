package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.subject.Subject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByMemberIdAndIsActiveTrue(Long memberId);

    Optional<Subject> findByMemberIdAndName(Long memberId, String name);

    boolean existsByName(String name);
}
