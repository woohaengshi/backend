package com.woohaengshi.backend.repository.statistics;

import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StatisticsCustomRepository {
    public int getMemberRank(StatisticsType statisticsType, Statistics statistics);
    public List<Statistics> filterAndSortStatisticsByType(StatisticsType statisticsType, Pageable pageable);
}
