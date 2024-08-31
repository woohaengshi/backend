package com.woohaengshi.backend.repository.statistics;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import static com.woohaengshi.backend.domain.statistics.QStatistics.*;

import java.util.List;

@RequiredArgsConstructor
public class StatisticsCustomRepositoryImpl implements StatisticsCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public int getMemberRank(StatisticsType statisticsType, Statistics memberStatistics) {
        NumberPath<Integer> timePath = Expressions.numberPath(Integer.class, statistics, statisticsType.getFieldName());
        int time = (statisticsType == StatisticsType.WEEKLY) ? memberStatistics.getWeeklyTime() : memberStatistics.getMonthlyTime();

        long count = jpaQueryFactory
                .selectFrom(statistics)
                .where(timePath.gt(time).and(timePath.ne(0)))
                .fetchCount();

        return (int) count + 1;
    }

    public List<Statistics> findStatisticsByTypeSortedByTimeDesc(StatisticsType statisticsType, Pageable pageable) {
        NumberPath<Integer> timePath = Expressions.numberPath(Integer.class, statistics, statisticsType.getFieldName());

        List<Statistics> results = jpaQueryFactory
                .selectFrom(statistics)
                .where(timePath.ne(0))
                .orderBy(timePath.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return results;
    }

    public long getCountStatisticsByType(StatisticsType statisticsType) {
        NumberPath<Integer> timePath = Expressions.numberPath(Integer.class, statistics, statisticsType.getFieldName());
        return jpaQueryFactory
                .select(timePath.count())
                .from(statistics)
                .where(timePath.ne(0))
                .fetchOne();
    }

}
