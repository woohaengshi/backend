package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.ShowRankSnapshotResponse;

import org.springframework.data.domain.Pageable;

public interface StatisticsService {
    ShowRankSnapshotResponse showRankData(
            Long memberId, StatisticsType statisticsType, Pageable pageable);
}
