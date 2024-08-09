package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.RankingSnapshotResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StatisticsService {
    int findMemberRanking(long memberId, StatisticsType statisticsType);

    Slice<Statistics> findStatisticsRankingData(StatisticsType statisticsType, Pageable pageable);

    RankingSnapshotResponse findRankingDataWithMember(
            long memberId, StatisticsType statisticsType, Pageable pageable);
}
