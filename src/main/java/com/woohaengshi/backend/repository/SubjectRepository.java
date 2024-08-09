package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
  boolean existsByMemberIdAndName(Long memberId, String name);
}
