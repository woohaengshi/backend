package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepository
        extends JpaRepository<Statistics, Long>, JpaSpecificationExecutor<Statistics> {

    @Query("SELECT s FROM Statistics s JOIN FETCH s.member WHERE s.member.id = :memberId")
    Optional<Statistics> findByMemberId(Long memberId);

    @Query("SELECT s FROM Statistics s JOIN FETCH s.member")
    List<Statistics> findAllWithMember();

    static Specification<Statistics> filterStatisticsWithTimeGreaterThan(
            StatisticsType type, int time) {
        return (root, query, cb) -> {
            Predicate timePredicate = cb.greaterThan(root.get(type.getFieldName()), time);
            Predicate timeIsNotZero = cb.notEqual(root.get(type.getFieldName()), 0);
            return cb.and(timePredicate, timeIsNotZero);
        };
    }

    static Specification<Statistics> filterAndSortStatisticsByType(StatisticsType statisticsType) {
        return (root, query, cb) -> {
            String fieldName = statisticsType.getFieldName();
            Predicate timeIsNotZero = cb.notEqual(root.get(fieldName), 0);
            query.orderBy(cb.desc(root.get(fieldName)));
            query.where(timeIsNotZero);
            return query.getRestriction();
        };
    }
}
