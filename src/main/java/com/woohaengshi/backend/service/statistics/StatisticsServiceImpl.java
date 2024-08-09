package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.StatisticsReadDto;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public int getMemberRanking(long memberId, StatisticsType statisticsType) {
        Statistics statistics =
                statisticsRepository
                        .findByMemberId(memberId)
                        .orElseThrow(() -> new WoohaengshiException(ErrorCode.STATISTICS_NOT_FOUND));
        int time = getTimeByStatisticsType(statisticsType, statistics);
        return getCountByTimeGreaterThan(statisticsType, time) + 1;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Statistics> getStatisticsRankingData(StatisticsType statisticsType, Pageable pageable) {
        Specification<Statistics> specification = (Root<Statistics> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if ("daily".equalsIgnoreCase(statisticsType.toString())) {
                query.orderBy(cb.desc(root.get("dailyTime")));
            } else if ("weekly".equalsIgnoreCase(statisticsType.toString())) {
                query.orderBy(cb.desc(root.get("weeklyTime")));
            } else if ("monthly".equalsIgnoreCase(statisticsType.toString())) {
                query.orderBy(cb.desc(root.get("monthlyTime")));
            }
            return query.getRestriction();
        };
        return statisticsRepository.findAll(specification, pageable);
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


    public int getCountByTimeGreaterThan(StatisticsType statisticsType, int time) {
        Specification<Statistics> specification =
                (root, query, cb) -> {
                    if (statisticsType == StatisticsType.DAILY) {
                        return cb.greaterThan(root.get("dailyTime"), time);
                    } else if (statisticsType == StatisticsType.WEEKLY) {
                        return cb.greaterThan(root.get("weeklyTime"), time);
                    } else if (statisticsType == StatisticsType.MONTHLY) {
                        return cb.greaterThan(root.get("monthlyTime"), time);
                    } else {
                        throw new WoohaengshiException(ErrorCode.STATISTICS_TYPE_NOT_FOUND);
                    }
                };

        return (int) statisticsRepository.count(specification);
    }

    private int getTimeByStatisticsType(StatisticsType statisticsType, Statistics statistics) {
        if (statisticsType == StatisticsType.DAILY)  return statistics.getDailyTime();
        else if (statisticsType == StatisticsType.WEEKLY) return statistics.getWeeklyTime();
        else return statistics.getMonthlyTime();
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
