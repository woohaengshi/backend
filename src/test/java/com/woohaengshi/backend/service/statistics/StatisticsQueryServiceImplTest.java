package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.domain.member.Course;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.member.State;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.StatisticsReadDto;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StatisticsQueryServiceImplTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private StatisticsQueryServiceImpl statisticsQueryService;

    private Member member1;
    private Member member2;
    private Statistics statistics1;
    private Statistics statistics2;

    @BeforeEach
    void setUp() {
        member1 = createMember(1L, "hong", "hong@example.com", "profile.jpg");
        member2 = createMember(2L, "kim", "kim@example.com", "profile2.jpg");

        statistics1 = createStatistics(1L, member1, 120, 30, 5, 500);
        statistics2 = createStatistics(2L, member2, 100, 20, 4, 400);
    }

    @Test
    void 멤버의_시간을_입력하여_등수를_찾을_수_있다() {
        // Given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member1));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member2));
        when(statisticsRepository.findByMemberId(1L)).thenReturn(Optional.of(statistics1));
        when(statisticsRepository.findByMemberId(2L)).thenReturn(Optional.of(statistics2));

        when(statisticsRepository.checkByDailyTimeRanking(5)).thenReturn(1);
        when(statisticsRepository.checkByDailyTimeRanking(4)).thenReturn(0);

        // When
        int ranking1 = statisticsQueryService.getMemberRanking(1L, StatisticsType.DAILY);
        int ranking2 = statisticsQueryService.getMemberRanking(2L, StatisticsType.DAILY);

        // Then
        assertEquals(2, ranking1);
        assertEquals(1, ranking2);
    }

    @Test
    void 통계_데이터_랭킹_조회() {
        // Given
        Slice<Statistics> expectedSlice = new SliceImpl<>(List.of(statistics1, statistics2));
        PageRequest pageable = PageRequest.of(0, 10);

        when(statisticsRepository.findAllByDailyTimeTimeRanking(pageable)).thenReturn(expectedSlice);

        // When
        Slice<Statistics> result = statisticsQueryService.getStatisticsRankingData(StatisticsType.DAILY, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getNumberOfElements());
        assertEquals(statistics1, result.getContent().get(0));
        assertEquals(statistics2, result.getContent().get(1));
    }

    @Test
    void 멤버와_함께_랭킹_데이터를_조회할_수_있다() {
        // Given
        PageRequest pageable = PageRequest.of(0, 10);
        Slice<Statistics> expectedSlice = new SliceImpl<>(List.of(statistics1, statistics2));

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member1));
        when(statisticsRepository.findByMemberId(1L)).thenReturn(Optional.of(statistics1));
        when(statisticsRepository.findAllByDailyTimeTimeRanking(pageable)).thenReturn(expectedSlice);
        when(statisticsQueryService.getMemberRanking(1L, StatisticsType.DAILY)).thenReturn(1);

        // When
        StatisticsReadDto result = statisticsQueryService.getRankingDataWithMember(1L, StatisticsType.DAILY, pageable);

        // Then
        assertNotNull(result);
        assertEquals(member1.getName(), result.getMember().getName());
        assertEquals(2, result.getMember().getRank());
        assertEquals(statistics1.getDailyTime(), result.getMember().getDailyTime());
        assertEquals(statistics1.getTotalTime(), result.getMember().getTotalTime());
        assertFalse(result.getInfiniteScrolling().isHasNext());
        assertEquals(2, result.getInfiniteScrolling().getRanks().size());

    }


    private Member createMember(Long id, String name, String email, String image) {
        return Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .password("securepassword")
                .image(image)
                .course(Course.AI_ENGINEERING)
                .state(State.ACTIVE)
                .sleepDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Statistics createStatistics(Long id, Member member, int monthlyTime, int weeklyTime, int dailyTime, int totalTime) {
        return Statistics.builder()
                .id(id)
                .monthlyTime(monthlyTime)
                .weeklyTime(weeklyTime)
                .dailyTime(dailyTime)
                .totalTime(totalTime)
                .member(member)
                .build();
    }
}
