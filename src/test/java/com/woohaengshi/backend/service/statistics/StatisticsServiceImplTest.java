package com.woohaengshi.backend.service.statistics;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.statistics.ShowRankSnapshotResponse;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.StatisticsRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
import com.woohaengshi.backend.support.fixture.MemberFixture;
import com.woohaengshi.backend.support.fixture.StatisticsFixture;
import com.woohaengshi.backend.support.fixture.StudyRecordFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceImplTest {

    @Mock private StatisticsRepository statisticsRepository;
    @Mock private StudyRecordRepository studyRecordRepository;
    @InjectMocks private StatisticsServiceImpl statisticsService;

    @Test
    void 랭킹을_조회한다() {
        Member member = MemberFixture.builder().id(1L).build();
        Statistics statistics = StatisticsFixture.builder().id(1L).member(member).build();
        StatisticsType statisticsType = StatisticsType.DAILY;
        Pageable pageable = PageRequest.of(0, 10);
        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(statisticsRepository.count(any(Specification.class))).willReturn(0L);
        given(statisticsRepository.findAll(any(Specification.class), eq(pageable)))
                .willReturn(new PageImpl<>(List.of(statistics)));

        ShowRankSnapshotResponse response =
                statisticsService.showRankData(member.getId(), statisticsType, pageable);

        // 응답 검증
        assertAll(
                "응답 전체 확인",
                () -> assertNotNull(response, "응답은 null이 아니어야 함"),
                () -> assertEquals(1, response.getMember().getRank(), "순위가 올바르게 계산되어야 함"),
                () -> assertFalse(response.getRanking().getHasNext(), "다음 페이지 존재 여부 확인"));
    }

    @Test
    void 회원이_존재하지_않으면_예외를_던진다() {
        Pageable pageable = PageRequest.of(0, 10);
        long nonExistentMemberId = 999L;
        StatisticsType statisticsType = StatisticsType.DAILY;

        when(statisticsRepository.findByMemberId(nonExistentMemberId)).thenReturn(Optional.empty());

        // 실행 & 검증
        assertThrows(
                WoohaengshiException.class,
                () -> {
                    statisticsService.showRankData(nonExistentMemberId, statisticsType, pageable);
                });
    }

    @Test
    void 스케쥴링_업데이트() {
        LocalDate today = LocalDate.of(2024, 8, 15);
        LocalDate yesterday = today.minusDays(1);
        Member member = MemberFixture.builder().id(1L).build();
        Statistics statistics =
                StatisticsFixture.builder()
                        .id(1L)
                        .member(member)
                        .weeklyTime(5)
                        .monthlyTime(10)
                        .totalTime(90)
                        .build();
        StudyRecord studyRecord =
                StudyRecordFixture.builder().id(1L).member(member).date(yesterday).time(10).build();
        List<Statistics> statisticsList = Collections.singletonList(statistics);

        when(statisticsRepository.findAllWithMember()).thenReturn(statisticsList);
        when(studyRecordRepository.findByDateAndMemberId(yesterday, 1L))
                .thenReturn(Optional.of(studyRecord));

        statisticsService.updateStatisticsTime(StatisticsType.WEEKLY);
        statisticsService.updateStatisticsTime(StatisticsType.MONTHLY);
        statisticsService.updateStatisticsTime(StatisticsType.TOTAL);

        // 응답 검증
        assertAll(
                "응답 전체 확인",
                () -> assertEquals(15, statistics.getWeeklyTime(), "주간 시간이 올바르게 업데이트 되어야 한다"),
                () ->
                        assertEquals(
                                20,
                                statistics.getMonthlyTime(),
                                "월간 시간이 올바르게 업데이트 되어야 한다 (1일기 이기 때문에 초기화도 이루어줘야 한다)"),
                () -> assertEquals(100, statistics.getTotalTime(), "통합 시간이 올바르게 업데이트 되어야 한다"));
    }

    @Test
    void 스케쥴링_업데이트_초기화_진행_확인() {
        LocalDate today = LocalDate.of(2024, 7, 1);
        LocalDate yesterday = today.minusDays(1);
        Member member = MemberFixture.builder().id(1L).build();
        Statistics statistics =
                StatisticsFixture.builder()
                        .id(1L)
                        .member(member)
                        .weeklyTime(5)
                        .monthlyTime(10)
                        .totalTime(90)
                        .build();
        StudyRecord studyRecord =
                StudyRecordFixture.builder().id(1L).member(member).date(yesterday).time(10).build();
        List<Statistics> statisticsList = Collections.singletonList(statistics);

        when(statisticsRepository.findAllWithMember()).thenReturn(statisticsList);
        when(studyRecordRepository.findByDateAndMemberId(yesterday, 1L))
                .thenReturn(Optional.of(studyRecord));

        statisticsService.updateStatisticsTime(StatisticsType.WEEKLY);
        statisticsService.updateStatisticsTime(StatisticsType.MONTHLY);
        statisticsService.updateStatisticsTime(StatisticsType.TOTAL);

        // 응답 검증
        assertAll(
                "응답 전체 확인",
                () -> assertEquals(10, statistics.getWeeklyTime(), "주간 시간이 올바르게 업데이트 되어야 한다 (월요일 이기 때문에 초기화도 이루어줘야 한다)"),
                () ->
                        assertEquals(
                                10,
                                statistics.getMonthlyTime(),
                                "월간 시간이 올바르게 업데이트 되어야 한다 (1일기 이기 때문에 초기화도 이루어줘야 한다)"),
                () -> assertEquals(100, statistics.getTotalTime(), "통합 시간이 올바르게 업데이트 되어야 한다"));
    }
}
