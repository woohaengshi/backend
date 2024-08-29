package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.constant.StandardTimeConstant;
import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.statistics.RankDataResponse;
import com.woohaengshi.backend.dto.response.statistics.ShowRankSnapshotResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.StatisticsRepository;
import com.woohaengshi.backend.repository.StatisticsSpecification;
import com.woohaengshi.backend.repository.StudyRecordSpecification;
import com.woohaengshi.backend.repository.studyrecord.StudyRecordRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
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
        LocalDate today = getShowDate();

        Optional<StudyRecord> studyRecord =
                studyRecordRepository.findByDateAndMemberId(today, memberId);
        Slice<StudyRecord> rankSlice = getDailyRankDataSlice(today, pageable);

        int rank = studyRecord.map(record -> findDailyRank(today, record.getTime())).orElse(0);
        int time = studyRecord.map(StudyRecord::getTime).orElse(0);

        return buildRankSnapshotResponse(statistics, rank, time, rankSlice, pageable);
    }

    private LocalDate getShowDate() {
        LocalTime nowTime = LocalTime.now();
        LocalDate today = LocalDate.now();

        if (nowTime.isBefore(StandardTimeConstant.STANDARD_TIME)) {
            return today.minusDays(1);
        }

        return today;
    }

    private ShowRankSnapshotResponse handlePeriodicStatistics(
            StatisticsType statisticsType, Pageable pageable, Statistics statistics) {
        Slice<Statistics> rankSlice = getPeriodicRankDataSlice(statisticsType, pageable);
        int studyTime = getTimeByStatisticsType(statisticsType, statistics);
        int rank = studyTime > 0 ?  statisticsRepository.getMemberRank(statisticsType, statistics) : 0;

        return buildRankSnapshotResponse(
                statistics, rank, studyTime, rankSlice, pageable, statisticsType);
    }

    private int findDailyRank(LocalDate date, int time) {
        return studyRecordRepository.findRankByDate(date, time);
    }

    private ShowRankSnapshotResponse buildRankSnapshotResponse(
            Statistics statistics,
            int rank,
            int time,
            Slice<StudyRecord> rankSlice,
            Pageable pageable) {
        return ShowRankSnapshotResponse.of(
                statistics.getMember(),
                rank,
                time,
                statistics.getTotalTime(),
                rankSlice.hasNext(),
                calculateDailyRank(rankSlice, pageable));
    }

    private ShowRankSnapshotResponse buildRankSnapshotResponse(
            Statistics statistics,
            int rank,
            int time,
            Slice<Statistics> rankSlice,
            Pageable pageable,
            StatisticsType statisticsType) {
        return ShowRankSnapshotResponse.of(
                statistics.getMember(),
                rank,
                time,
                statistics.getTotalTime(),
                rankSlice.hasNext(),
                calculatePeriodicRank(rankSlice, pageable, statisticsType));
    }

    public Slice<Statistics> getPeriodicRankDataSlice(StatisticsType statisticsType, Pageable pageable) {
        Pageable extendedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() + 1, pageable.getSort());
        List<Statistics> content = statisticsRepository.filterAndSortStatisticsByType(statisticsType, extendedPageable);

        boolean hasNext = content.size() > pageable.getPageSize();

        List<Statistics> pageContent = hasNext ? content.subList(0, pageable.getPageSize()) : content;

        return new SliceImpl<>(pageContent, pageable, hasNext);
    }


    private Slice<StudyRecord> getDailyRankDataSlice(LocalDate targetDate, Pageable pageable) {
        Specification<StudyRecord> spec =
                StudyRecordSpecification.findStudyRecordsByDateSortedByTimeDesc(targetDate);
        return studyRecordRepository.findAll(spec, pageable);
    }

    private List<RankDataResponse> calculateDailyRank(
            Slice<StudyRecord> rankSlice, Pageable pageable) {
        int startRank = pageable.getPageNumber() * pageable.getPageSize() + 1;

        return IntStream.range(0, rankSlice.getNumberOfElements())
                .mapToObj(
                        index -> {
                            StudyRecord studyRecord = rankSlice.getContent().get(index);
                            Member member = studyRecord.getMember();
                            Statistics statistics = findStatisticsByMemberId(member.getId());

                            return RankDataResponse.of(
                                    member,
                                    startRank + index,
                                    studyRecord.getTime(),
                                    statistics.getTotalTime());
                        })
                .toList();
    }

    private List<RankDataResponse> calculatePeriodicRank(
            Slice<Statistics> rankSlice, Pageable pageable, StatisticsType statisticsType) {
        int startRank = pageable.getPageNumber() * pageable.getPageSize() + 1;

        return IntStream.range(0, rankSlice.getNumberOfElements())
                .mapToObj(
                        index -> {
                            Statistics statistics = rankSlice.getContent().get(index);
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
