package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.FindRankingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StatisticsService {
    int getMemberRanking(long memberId, StatisticsType statisticsType);

    Slice<Statistics> getStatisticsRankingData(StatisticsType statisticsType, Pageable pageable);

    FindRankingResponse getRankingDataWithMember(
            long memberId, StatisticsType statisticsType, Pageable pageable);
}
