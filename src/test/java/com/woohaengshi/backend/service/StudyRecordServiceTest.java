package com.woohaengshi.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.StudySubject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.request.studyrecord.EditSubjectAndCommentRequest;
import com.woohaengshi.backend.dto.request.studyrecord.SaveCommentRequest;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;
import com.woohaengshi.backend.dto.response.studyrecord.ShowYearlyRecordResponse;
import com.woohaengshi.backend.dto.result.MonthlyTotalRecordResult;
import com.woohaengshi.backend.dto.result.ShowCalendarResult;
import com.woohaengshi.backend.dto.result.SubjectResult;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudySubjectRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.repository.statistics.StatisticsRepository;
import com.woohaengshi.backend.repository.studyrecord.StudyRecordRepository;
import com.woohaengshi.backend.service.studyrecord.StudyRecordServiceImpl;
import com.woohaengshi.backend.support.fixture.MemberFixture;
import com.woohaengshi.backend.support.fixture.StatisticsFixture;
import com.woohaengshi.backend.support.fixture.StudyRecordFixture;
import com.woohaengshi.backend.support.fixture.SubjectFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StudyRecordServiceTest {
    @Mock private MemberRepository memberRepository;
    @Mock private StudyRecordRepository studyRecordRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private StudySubjectRepository studySubjectRepository;
    @Mock private StatisticsRepository statisticsRepository;
    @InjectMocks private StudyRecordServiceImpl studyRecordService;

    @Test
    void 첫_공부_기록을_저장할_수_있다() {
        int MONTHLY_TIME = 100;
        Member member = MemberFixture.builder().id(1L).build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));
        StudyRecord studyRecord = StudyRecordFixture.from(request, 1L);
        Statistics statistics =
                StatisticsFixture.builder().monthlyTime(MONTHLY_TIME).member(member).id(1L).build();

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(studyRecordRepository.findByDateAndMemberId(request.getDate(), member.getId()))
                .willReturn(Optional.empty());
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(studyRecordRepository.save(any(StudyRecord.class))).willReturn(studyRecord);
        for (Long subjectId : request.getSubjects()) {
            given(
                            studySubjectRepository.existsBySubjectIdAndStudyRecordId(
                                    subjectId, studyRecord.getId()))
                    .willReturn(false);
            given(subjectRepository.findById(subjectId))
                    .willReturn(Optional.of(Subject.builder().id(subjectId).build()));
        }

        assertAll(
                () -> studyRecordService.save(request, member.getId()),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(request.getDate(), member.getId()),
                () -> verify(memberRepository, times(1)).findById(member.getId()),
                () -> verify(studyRecordRepository, times(1)).save(any(StudyRecord.class)),
                () -> verify(subjectRepository, times(2)).findById(any(Long.class)),
                () -> verify(studySubjectRepository, times(2)).save(any(StudySubject.class)),
                () ->
                        assertThat(statistics.getMonthlyTime())
                                .isEqualTo(MONTHLY_TIME + request.getTime()));
    }

    @Test
    void 이미_공부한_과목의_기록을_누적해_저장할_수_있다() {
        int MONTHLY_TIME = 100;
        Member member = MemberFixture.builder().id(1L).build();
        StudyRecord existStudyRecord =
                StudyRecordFixture.builder().member(member).id(1L).time(20).build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 30, List.of(1L, 2L));
        Statistics statistics =
                StatisticsFixture.builder().monthlyTime(MONTHLY_TIME).member(member).id(1L).build();

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(studyRecordRepository.findByDateAndMemberId(request.getDate(), member.getId()))
                .willReturn(Optional.of(existStudyRecord));
        request.getSubjects()
                .forEach(
                        subjectId -> {
                            given(
                                            studySubjectRepository
                                                    .existsBySubjectIdAndStudyRecordId(
                                                            subjectId, existStudyRecord.getId()))
                                    .willReturn(true);
                        });

        assertAll(
                () -> studyRecordService.save(request, member.getId()),
                () -> assertThat(existStudyRecord.getTime()).isEqualTo(30),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(request.getDate(), member.getId()),
                () ->
                        verify(studySubjectRepository, times(2))
                                .existsBySubjectIdAndStudyRecordId(
                                        any(Long.class), any(Long.class)),
                () -> verify(studySubjectRepository, never()).save(any(StudySubject.class)),
                () -> assertThat(statistics.getMonthlyTime()).isEqualTo(MONTHLY_TIME + 10));
    }

    @Test
    void 처음_공부한_과목의_기록을_누적해_저장할_수_있다() {
        int MONTHLY_TIME = 100;
        Member member = MemberFixture.builder().id(1L).build();
        StudyRecord existStudyRecord =
                StudyRecordFixture.builder().member(member).id(1L).time(20).build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 30, List.of(1L, 2L));
        Statistics statistics =
                StatisticsFixture.builder().monthlyTime(MONTHLY_TIME).member(member).id(1L).build();

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(statisticsRepository.findByMemberId(member.getId()))
                .willReturn(Optional.of(statistics));
        given(studyRecordRepository.findByDateAndMemberId(request.getDate(), member.getId()))
                .willReturn(Optional.of(existStudyRecord));
        request.getSubjects()
                .forEach(
                        subjectId -> {
                            given(
                                            studySubjectRepository
                                                    .existsBySubjectIdAndStudyRecordId(
                                                            subjectId, existStudyRecord.getId()))
                                    .willReturn(false);
                            given(subjectRepository.findById(subjectId))
                                    .willReturn(
                                            Optional.of(Subject.builder().id(subjectId).build()));
                        });

        assertAll(
                () -> studyRecordService.save(request, member.getId()),
                () -> assertThat(existStudyRecord.getTime()).isEqualTo(30),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(request.getDate(), member.getId()),
                () ->
                        verify(studySubjectRepository, times(2))
                                .existsBySubjectIdAndStudyRecordId(
                                        any(Long.class), any(Long.class)),
                () -> verify(subjectRepository, times(2)).findById(any(Long.class)),
                () -> verify(studySubjectRepository, times(2)).save(any(StudySubject.class)),
                () -> assertThat(statistics.getMonthlyTime()).isEqualTo(MONTHLY_TIME + 10));
    }

    @Test
    void 월_단위_공부_기록을_조회_할_수_있다() {
        YearMonth date = YearMonth.now();

        SubjectResult subjectResult1 = new SubjectResult(1L, "HTML");
        SubjectResult subjectResult2 = new SubjectResult(2L, "CSS");
        SubjectResult subjectResult3 = new SubjectResult(3L, "JAVA");

        ShowCalendarResult showCalendarResult1 =
                new ShowCalendarResult(12, 10, "1", List.of(subjectResult1, subjectResult2));
        ShowCalendarResult showCalendarResult2 =
                new ShowCalendarResult(13, 100, "2", List.of(subjectResult3));
        ShowCalendarResult showCalendarResult3 =
                new ShowCalendarResult(14, 200, "3", List.of(subjectResult1, subjectResult3));

        List<ShowCalendarResult> result =
                List.of(showCalendarResult1, showCalendarResult2, showCalendarResult3);

        given(memberRepository.existsById(1L)).willReturn(true);
        given(
                        studyRecordRepository.findStudyRecordInCalendar(
                                date.getYear(), date.getMonthValue(), 1L))
                .willReturn(result);
        given(subjectRepository.findAllByMemberIdAndIsActiveTrue(1L))
                .willReturn(List.of(SubjectFixture.builder().build()));

        ShowMonthlyRecordResponse response = studyRecordService.getMonthlyRecord(date, 1L);
        assertAll(
                () -> assertThat(response.getYear()).isEqualTo(date.getYear()),
                () -> assertThat(response.getMonth()).isEqualTo(date.getMonthValue()),
                () ->
                        assertThat(response.getRecords().size())
                                .isEqualTo(date.atEndOfMonth().getDayOfMonth()),
                () -> assertThat(response.getRecords().get(0).getTime()).isEqualTo(0),
                () ->
                        assertThat(response.getRecords().get(11).getTime())
                                .isEqualTo(showCalendarResult1.getTime()),
                () ->
                        assertThat(response.getRecords().get(12).getTime())
                                .isEqualTo(showCalendarResult2.getTime()),
                () ->
                        assertThat(response.getRecords().get(13).getTime())
                                .isEqualTo(showCalendarResult3.getTime()),
                () ->
                        assertThat(response.getRecords().get(12).getComment())
                                .isEqualTo(showCalendarResult1.getComment()),
                () ->
                        assertThat(response.getTotalSubjects().get(0).getId())
                                .isEqualTo(SubjectFixture.builder().build().getId()));
    }

    @Test
    void 해당하는_연도의_월_별_공부_기록을_조회_한다() {

        List<MonthlyTotalRecordResult> expected = new ArrayList<>();
        expected.add(new MonthlyTotalRecordResult(1, 100L));
        expected.add(new MonthlyTotalRecordResult(2, 200L));

        given(memberRepository.existsById(1L)).willReturn(true);
        given(studyRecordRepository.findMonthlyTotalByYearAndMemberId(2024, 1L))
                .willReturn(expected);

        ShowYearlyRecordResponse response = studyRecordService.showYearlyRecord(2024, 1L);

        assertAll(
                "response",
                () -> assertThat(response.getYear()).isEqualTo(2024),
                () -> {
                    for (int i = 0; i < response.getRecords().size(); i++) {
                        assertThat(response.getRecords().get(i).getMonth())
                                .isEqualTo(expected.get(i).getMonth());
                        assertThat(response.getRecords().get(i).getTotal())
                                .isEqualTo(expected.get(i).getTotal());
                    }
                });
    }

    @Test
    void 회원이_존재하지_않을_경우_예외를_던진다() {
        Member member = MemberFixture.builder().build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(studyRecordRepository.findByDateAndMemberId(request.getDate(), member.getId()))
                .willReturn(Optional.empty());
        given(memberRepository.findById(member.getId())).willReturn(Optional.empty());

        assertAll(
                () ->
                        assertThatThrownBy(() -> studyRecordService.save(request, member.getId()))
                                .isExactlyInstanceOf(WoohaengshiException.class),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(request.getDate(), member.getId()),
                () -> verify(memberRepository, times(1)).findById(member.getId()));
    }

    @Test
    void 공부기록이_존재할_경우_회고를_업데이트_한다() {
        Member member = MemberFixture.builder().id(1L).build();
        SaveCommentRequest request = new SaveCommentRequest(LocalDate.now(), "회고1");

        StudyRecord defaultStudyRecord = mock(StudyRecord.class);

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(studyRecordRepository.findByDateAndMemberId(any(LocalDate.class), any(Long.class)))
                .willReturn(Optional.of(defaultStudyRecord));

        assertAll(
                () -> studyRecordService.saveComment(request, 1L),
                () -> verify(memberRepository, times(1)).existsById(member.getId()),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(any(LocalDate.class), any(Long.class)),
                () -> verify(defaultStudyRecord, times(1)).updateComment(any(String.class)));
    }

    @Test
    void 공부기록이_존재하지_않을_경우_공부기록을_새롭게_추가한다() {
        Member member = MemberFixture.builder().id(1L).build();
        SaveCommentRequest request = new SaveCommentRequest(LocalDate.now(), "회고1");

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(studyRecordRepository.findByDateAndMemberId(any(LocalDate.class), any(Long.class)))
                .willReturn(Optional.empty());
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        assertAll(
                () -> studyRecordService.saveComment(request, 1L),
                () -> verify(memberRepository, times(1)).existsById(member.getId()),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(any(LocalDate.class), any(Long.class)),
                () -> verify(memberRepository, times(1)).findById(member.getId()),
                () -> verify(studyRecordRepository, times(1)).save(any(StudyRecord.class)));
    }

    @Test
    void 회고와_공부한_과목을_수정한다() {
        Member member = MemberFixture.builder().id(1L).build();
        StudyRecord defaultStudyRecord = mock(StudyRecord.class);

        EditSubjectAndCommentRequest editRequest =
                new EditSubjectAndCommentRequest(LocalDate.now(), List.of(2L), List.of(3L), "회고수정");

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(studyRecordRepository.findByDateAndMemberId(any(LocalDate.class), any(Long.class)))
                .willReturn(Optional.of(defaultStudyRecord));
        given(defaultStudyRecord.getId()).willReturn(1L);
        given(subjectRepository.findById(2L))
                .willReturn(Optional.of(Subject.builder().id(2L).build()));
        given(
                        studySubjectRepository.existsBySubjectIdAndStudyRecordId(
                                2L, defaultStudyRecord.getId()))
                .willReturn(false);
        given(
                        studySubjectRepository.existsBySubjectIdAndStudyRecordId(
                                3L, defaultStudyRecord.getId()))
                .willReturn(true);

        assertAll(
                () -> studyRecordService.editSubjectsAndComment(editRequest, member.getId()),
                () -> verify(memberRepository, times(1)).existsById(member.getId()),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(any(LocalDate.class), any(Long.class)),
                () ->
                        verify(studySubjectRepository, times(1))
                                .existsBySubjectIdAndStudyRecordId(2L, defaultStudyRecord.getId()),
                () -> verify(studySubjectRepository, times(1)).save(any(StudySubject.class)),
                () ->
                        verify(studySubjectRepository, times(1))
                                .existsBySubjectIdAndStudyRecordId(3L, defaultStudyRecord.getId()),
                () ->
                        verify(studySubjectRepository, times(1))
                                .deleteBySubjectIdAndStudyRecordId(3L, defaultStudyRecord.getId()),
                () -> verify(defaultStudyRecord, times(1)).updateComment(any(String.class)));
    }
}
