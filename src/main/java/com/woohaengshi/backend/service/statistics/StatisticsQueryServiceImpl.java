package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.StatisticsReadDto;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StatisticsQueryServiceImpl implements StatisticsQueryService {
    private final StatisticsRepository statisticsRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public int getMemberRanking(long memberId, StatisticsType statisticsType) {
        Statistics statistics =
                statisticsRepository
                        .findByMemberId(memberId)
                        .orElseThrow(() -> new WoohaengshiException(ErrorCode.STATISTICS_NOT_FOUND));
        int rank;
        switch (statisticsType) {
            case DAILY:
                rank = statisticsRepository.checkByDailyTimeRanking(statistics.getDailyTime());
                break;
            case WEEKLY:
                rank = statisticsRepository.checkByWeeklyTimeRanking(statistics.getWeeklyTime());
                break;
            case MONTHLY:
                rank = statisticsRepository.checkByMonthlyTimeRanking(statistics.getMonthlyTime());
                break;
            default:
                throw new WoohaengshiException(ErrorCode.STATISTICS_TYPE_NOT_FOUND);
        }

        return rank + 1;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Statistics> getStatisticsRankingData(
            StatisticsType statisticsType, Pageable pageable) {
        switch (statisticsType) {
            case DAILY:
                return statisticsRepository.findAllByDailyTimeTimeRanking(pageable);
            case WEEKLY:
                return statisticsRepository.findAllByWeeklyTimeRanking(pageable);
            case MONTHLY:
                return statisticsRepository.findAllByMonthlyTimeRanking(pageable);
            default:
                throw new WoohaengshiException(ErrorCode.STATISTICS_TYPE_NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticsReadDto getRankingDataWithMember(
            long memberId, StatisticsType statisticsType, Pageable pageable) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new WoohaengshiException(ErrorCode.MEMBER_NOT_FOUND));

        Statistics statistics =
                statisticsRepository
                        .findByMemberId(memberId)
                        .orElseThrow(() -> new WoohaengshiException(ErrorCode.STATISTICS_NOT_FOUND));


        int memberRanking = getMemberRanking(memberId, statisticsType);
        Slice<Statistics> statisticsRankingData =
                getStatisticsRankingData(statisticsType, pageable);

        List<StatisticsReadDto.MemberRankDto> memberRankDtos =
                createMemberRankDtos(statisticsRankingData, pageable, statisticsType);

        return StatisticsReadDto.of(
                member,
                memberRanking,
                statistics.getDailyTime(),
                statistics.getTotalTime(),
                statisticsRankingData.hasNext(),
                memberRankDtos);
    }

    private int getTimeByStatisticsType(StatisticsType statisticsType, Statistics statistics) {
        switch (statisticsType) {
            case DAILY:
                return statistics.getDailyTime();
            case WEEKLY:
                return statistics.getWeeklyTime();
            case MONTHLY:
                return statistics.getMonthlyTime();
            default:
                throw new IllegalArgumentException("Invalid StatisticsType: " + statisticsType);
        }
    }

    private List<StatisticsReadDto.MemberRankDto> createMemberRankDtos(
            Slice<Statistics> statisticsSlice, Pageable pageable, StatisticsType statisticsType) {
        int startRank = pageable.getPageNumber() * pageable.getPageSize() + 1;

        return IntStream.range(0, statisticsSlice.getNumberOfElements())
                .mapToObj(
                        index -> {
                            Statistics statistics = statisticsSlice.getContent().get(index);
                            Member member = statistics.getMember();

                            return StatisticsReadDto.MemberRankDto.of(
                                    member.getId(),
                                    member.getName(),
                                    member.getImage(),
                                    member.getCourse().getName(),
                                    startRank + index,
                                    getTimeByStatisticsType(statisticsType, statistics),
                                    statistics.getTotalTime());
                        })
                .collect(Collectors.toList());
    }
}
