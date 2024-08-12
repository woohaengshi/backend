package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.statistics.RankDataResponse;
import com.woohaengshi.backend.dto.response.statistics.ShowRankSnapshotResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.StatisticsRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Override
    @Transactional(readOnly = true)
    public ShowRankSnapshotResponse showRankData(
            Long memberId, StatisticsType statisticsType, Pageable pageable) {
        Statistics statistics = getStatisticsByMemberId(memberId);
        Slice<Statistics> rankSlice = getRankDataSlice(statisticsType, pageable);

        return ShowRankSnapshotResponse.of(
                statistics.getMember(),
                getMemberRank(statisticsType, statistics),
                statistics.getDailyTime(),
                statistics.getTotalTime(),
                rankSlice.hasNext(),
                calculationRank(rankSlice, pageable, statisticsType));
    }

    private int getMemberRank(StatisticsType statisticsType, Statistics statistics) {
        int time = getTimeByStatisticsType(statisticsType, statistics);

        Specification<Statistics> specification =
                (Root<Statistics> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                    if (statisticsType == StatisticsType.DAILY)
                        return cb.greaterThan(root.get("dailyTime"), time);
                    if (statisticsType == StatisticsType.WEEKLY)
                        return cb.greaterThan(root.get("weeklyTime"), time);
                    if (statisticsType == StatisticsType.MONTHLY)
                        return cb.greaterThan(root.get("monthlyTime"), time);

                    throw new WoohaengshiException(ErrorCode.STATISTICS_TYPE_NOT_FOUND);
                };
        return (int) statisticsRepository.count(specification) + 1;
    }

    private Slice<Statistics> getRankDataSlice(StatisticsType statisticsType, Pageable pageable) {
        Specification<Statistics> specification =
                (Root<Statistics> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                    if (statisticsType == StatisticsType.DAILY)
                        query.orderBy(cb.desc(root.get("dailyTime")));
                    if (statisticsType == StatisticsType.WEEKLY)
                        query.orderBy(cb.desc(root.get("weeklyTime")));
                    if (statisticsType == StatisticsType.MONTHLY)
                        query.orderBy(cb.desc(root.get("monthlyTime")));
                    return query.getRestriction();
                };
        return statisticsRepository.findAll(specification, pageable);
    }

    private List<RankDataResponse> calculationRank(
            Slice<Statistics> statisticsSlice, Pageable pageable, StatisticsType statisticsType) {
        int startRank = pageable.getPageNumber() * pageable.getPageSize() + 1;

        return IntStream.range(0, statisticsSlice.getNumberOfElements())
                .mapToObj(
                        index -> {
                            Statistics statistics = statisticsSlice.getContent().get(index);
                            Member member = statistics.getMember();

                            return RankDataResponse.of(
                                    member,
                                    startRank + index,
                                    getTimeByStatisticsType(statisticsType, statistics),
                                    statistics.getTotalTime());
                        })
                .toList();
    }

    private int getTimeByStatisticsType(StatisticsType statisticsType, Statistics statistics) {
        if (statisticsType == StatisticsType.DAILY) return statistics.getDailyTime();
        if (statisticsType == StatisticsType.WEEKLY) return statistics.getWeeklyTime();
        if (statisticsType == StatisticsType.MONTHLY) return statistics.getMonthlyTime();

        throw new WoohaengshiException(ErrorCode.STATISTICS_TYPE_NOT_FOUND);
    }

    private Statistics getStatisticsByMemberId(Long memberId) {
        return statisticsRepository
                .findByMemberId(memberId)
                .orElseThrow(() -> new WoohaengshiException(ErrorCode.STATISTICS_NOT_FOUND));
    }
}
