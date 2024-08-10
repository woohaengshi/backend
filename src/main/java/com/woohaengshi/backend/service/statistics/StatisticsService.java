package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.RankingSnapshotResponse;

import org.springframework.data.domain.Pageable;

public interface StatisticsService {
    RankingSnapshotResponse showRankData(
            long memberId, StatisticsType statisticsType, Pageable pageable);
}
