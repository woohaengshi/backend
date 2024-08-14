package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.statistics.RankDataResponse;
import com.woohaengshi.backend.dto.response.statistics.ShowRankSnapshotResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.StatisticsRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final StudyRecordRepository studyRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public ShowRankSnapshotResponse showRankData(
            Long memberId, StatisticsType statisticsType, Pageable pageable) {
        Statistics statistics = findStatisticsByMemberId(memberId);
        return statisticsType == StatisticsType.DAILY
                ? handleDailyStatistics(memberId, pageable, statistics)
                : handlePeriodicStatistics(statisticsType, pageable, statistics);
    }

    private ShowRankSnapshotResponse handleDailyStatistics(
            Long memberId, Pageable pageable, Statistics statistics) {
        Optional<StudyRecord> studyRecord =
                studyRecordRepository.findByDateAndMemberId(LocalDate.now(), memberId);
        Slice<StudyRecord> rankSlice = getRankDataSlice(LocalDate.now(), pageable);
        return ShowRankSnapshotResponse.of(
                statistics.getMember(),
                (studyRecord.isPresent())
                        ? studyRecordRepository.findRankByDate(
                                LocalDate.now(), studyRecord.get().getTime())
                        : 0,
                (studyRecord.isPresent()) ? studyRecord.get().getTime() : 0,
                statistics.getTotalTime(),
                rankSlice.hasNext(),
                calculationRank(rankSlice, pageable, statistics));
    }

    private ShowRankSnapshotResponse handlePeriodicStatistics(
            StatisticsType statisticsType, Pageable pageable, Statistics statistics) {
        Slice<Statistics> rankSlice = getRankDataSlice(statisticsType, pageable);
        int studyTime = getTimeByStatisticsType(statisticsType, statistics);
        return ShowRankSnapshotResponse.of(
                statistics.getMember(),
                (studyTime == 0) ? 0 : getMemberRank(statisticsType, statistics),
                studyTime,
                statistics.getTotalTime(),
                rankSlice.hasNext(),
                (rankSlice.getContent().size() == 0)
                        ? Collections.emptyList()
                        : calculationRank(rankSlice, pageable, statisticsType));
    }

    public int getMemberRank(StatisticsType statisticsType, Statistics statistics) {
        int time = getTimeByStatisticsType(statisticsType, statistics);
        long count =
                statisticsRepository.count(
                        StatisticsRepository.filterStatisticsWithTimeGreaterThan(
                                statisticsType, time));
        return (int) count + 1;
    }

    private Slice<Statistics> getRankDataSlice(StatisticsType statisticsType, Pageable pageable) {
        Specification<Statistics> spec =
                StatisticsRepository.filterAndSortStatisticsByType(statisticsType);
        return statisticsRepository.findAll(spec, pageable);
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

    private Slice<StudyRecord> getRankDataSlice(LocalDate targetDate, Pageable pageable) {
        Specification<StudyRecord> specification =
                StudyRecordRepository.findStudyRecordsByDateSortedByTimeDesc(targetDate);
        return studyRecordRepository.findAll(specification, pageable);
    }

    private List<RankDataResponse> calculationRank(
            Slice<StudyRecord> studyRecordSlice, Pageable pageable, Statistics statistics) {
        int startRank = pageable.getPageNumber() * pageable.getPageSize() + 1;

        return IntStream.range(0, studyRecordSlice.getNumberOfElements())
                .mapToObj(
                        index -> {
                            StudyRecord studyRecord = studyRecordSlice.getContent().get(index);
                            Member member = studyRecord.getMember();

                            return RankDataResponse.of(
                                    member,
                                    startRank + index,
                                    studyRecord.getTime(),
                                    statistics.getTotalTime());
                        })
                .toList();
    }

    private int getTimeByStatisticsType(StatisticsType statisticsType, Statistics statistics) {
        if (statisticsType == StatisticsType.WEEKLY) return statistics.getWeeklyTime();
        if (statisticsType == StatisticsType.MONTHLY) return statistics.getMonthlyTime();

        throw new WoohaengshiException(ErrorCode.STATISTICS_TYPE_NOT_FOUND);
    }

    private Statistics findStatisticsByMemberId(Long memberId) {
        return statisticsRepository
                .findByMemberId(memberId)
                .orElseThrow(() -> new WoohaengshiException(ErrorCode.STATISTICS_NOT_FOUND));
    }
}
