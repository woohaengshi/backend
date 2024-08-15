package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.StudyRecord;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class StudyRecordSpecification {
    public static Specification<StudyRecord> findStudyRecordsByDateSortedByTimeDesc(LocalDate date) {
        return (root, query, cb) -> {
            Predicate datePredicate = cb.equal(root.get("date"), date);
            query.orderBy(cb.desc(root.get("time")));
            return datePredicate;
        };
    }
}
