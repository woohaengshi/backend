package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class StatisticsSpecification {
    public static Specification<Statistics> filterStatisticsWithTimeGreaterThan(
            StatisticsType type, int time) {
        return (root, query, cb) -> {
            Predicate timePredicate = cb.greaterThan(root.get(type.getFieldName()), time);
            Predicate timeIsNotZero = cb.notEqual(root.get(type.getFieldName()), 0);
            return cb.and(timePredicate, timeIsNotZero);
        };
    }

    public static Specification<Statistics> filterAndSortStatisticsByType(StatisticsType statisticsType) {
        return (root, query, cb) -> {
            String fieldName = statisticsType.getFieldName();
            Predicate timeIsNotZero = cb.notEqual(root.get(fieldName), 0);
            query.orderBy(cb.desc(root.get(fieldName)));
            query.where(timeIsNotZero);
            return query.getRestriction();
        };
    }
}
