package com.woohaengshi.backend.service.statistics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.statistics.ShowRankSnapshotResponse;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.statistics.StatisticsRepository;
import com.woohaengshi.backend.repository.studyrecord.StudyRecordRepository;
import com.woohaengshi.backend.support.fixture.MemberFixture;
import com.woohaengshi.backend.support.fixture.StatisticsFixture;
import com.woohaengshi.backend.support.fixture.StudyRecordFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceImplTest {

    @Mock private StatisticsRepository statisticsRepository;
    @Mock private StudyRecordRepository studyRecordRepository;

    @InjectMocks private StatisticsServiceImpl statisticsService;

    @Test
    void 일간_랭킹을_조회한다() {
        Member member = MemberFixture.builder().id(1L).build();
        Statistics statistics = StatisticsFixture.builder().id(1L).member(member).build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));
        StudyRecord studyRecord =
                StudyRecordFixture.builder()
                        .id(1L)
                        .member(member)
                        .time(request.getTime())
                        .date(request.getDate())
                        .build();
        Pageable pageable = PageRequest.of(0, 10);

        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(studyRecordRepository.findByDateAndMemberId(LocalDate.now(), member.getId()))
                .willReturn(Optional.of(studyRecord));
        given(
                        studyRecordRepository.findStudyRecordsByDateSortedByTimeDesc(
                                LocalDate.now(), pageable))
                .willReturn(new SliceImpl<>(List.of(studyRecord), pageable, false));

        ShowRankSnapshotResponse response =
                statisticsService.showRankData(member.getId(), StatisticsType.DAILY, pageable);

        // 응답 검증
        assertAll(
                "응답 전체 확인",
                () -> assertNotNull(response, "응답은 null이 아니어야 함"),
                () ->
                        assertEquals(
                                1,
                                response.getRanking().getRanks().get(0).getRank(),
                                "순위가 올바르게 계산되어야 함"),
                () -> assertFalse(response.getRanking().getHasNext(), "다음 페이지 존재 여부 확인"));
    }

    @Test
    void 주간_월간_랭킹을_조회한다() {
        Member member = MemberFixture.builder().id(1L).build();
        Statistics statistics = StatisticsFixture.builder().id(1L).member(member).build();
        Pageable pageable = PageRequest.of(0, 10);

        StatisticsType statisticsType = StatisticsType.WEEKLY;

        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(statisticsRepository.getMemberRank(statisticsType, statistics)).willReturn(1L);
        given(statisticsRepository.findStatisticsByTypeSortedByTimeDesc(statisticsType, pageable))
                .willReturn(new SliceImpl<>(List.of(statistics), pageable, false));
        ShowRankSnapshotResponse response =
                statisticsService.showRankData(member.getId(), statisticsType, pageable);

        // 응답 검증
        assertAll(
                "응답 전체 확인",
                () -> assertNotNull(response, "응답은 null이 아니어야 함"),
                () ->
                        assertEquals(
                                1,
                                response.getRanking().getRanks().get(0).getRank(),
                                "순위가 올바르게 계산되어야 함"),
                () -> assertFalse(response.getRanking().getHasNext(), "다음 페이지 존재 여부 확인"));
    }

    @Test
    void 일간_시간이_없는_경우_0등_0시간으로_나온다() {
        Member member = MemberFixture.builder().id(1L).build();
        Statistics statistics =
                StatisticsFixture.builder().id(1L).member(member).weeklyTime(0).build();
        Pageable pageable = PageRequest.of(0, 10);

        StatisticsType statisticsType = StatisticsType.DAILY;

        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(studyRecordRepository.findByDateAndMemberId(LocalDate.now(), member.getId()))
                .willReturn(Optional.empty());
        given(
                studyRecordRepository.findStudyRecordsByDateSortedByTimeDesc(
                        LocalDate.now(), pageable))
                .willReturn(new SliceImpl<>(List.of(), pageable, false));

        ShowRankSnapshotResponse response =
                statisticsService.showRankData(member.getId(), statisticsType, pageable);

        assertAll(
                () -> assertEquals(0, response.getMember().getRank(), "학습 기록이 없으면 0등으로 나온다"),
                () -> assertEquals(0, response.getMember().getStudyTime(), "학습 기록이 없으면 0 시간으로 나온다")
        );
    }

    @Test
    void 주간_월간_시간이_0인_경우_랭킹은_0이_나온다() {
        Member member = MemberFixture.builder().id(1L).build();
        Statistics statistics =
                StatisticsFixture.builder().id(1L).member(member).weeklyTime(0).build();
        Pageable pageable = PageRequest.of(0, 10);

        StatisticsType statisticsType = StatisticsType.WEEKLY;

        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(statisticsRepository.findStatisticsByTypeSortedByTimeDesc(statisticsType, pageable))
                .willReturn(new SliceImpl<>(List.of(statistics), pageable, false));
        ShowRankSnapshotResponse response =
                statisticsService.showRankData(member.getId(), statisticsType, pageable);

        assertAll(() -> assertEquals(0, response.getMember().getRank(), "0시간이면 0등으로 나온다"));
    }
    @Test
    void 페이지_숫자가_1이상이면_member_항목이_없다() {
        Member member = MemberFixture.builder().id(1L).build();
        Statistics statistics = StatisticsFixture.builder().id(1L).member(member).build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));
        StudyRecord studyRecord =
                StudyRecordFixture.builder()
                        .id(1L)
                        .member(member)
                        .time(request.getTime())
                        .date(request.getDate())
                        .build();
        Pageable pageable = PageRequest.of(1, 10);

        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(
                        studyRecordRepository.findStudyRecordsByDateSortedByTimeDesc(
                                LocalDate.now(), pageable))
                .willReturn(new SliceImpl<>(List.of(studyRecord), pageable, false));

        ShowRankSnapshotResponse response =
                statisticsService.showRankData(member.getId(), StatisticsType.DAILY, pageable);

        assertNull(response.getMember());
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
}
