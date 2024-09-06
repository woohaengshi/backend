package com.woohaengshi.backend.repository.statistics;

import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StatisticsCustomRepository {
    public long getMemberRank(StatisticsType statisticsType, Statistics statistics);

    public Slice<Statistics> findStatisticsByTypeSortedByTimeDesc(
            StatisticsType statisticsType, Pageable pageable);
}
