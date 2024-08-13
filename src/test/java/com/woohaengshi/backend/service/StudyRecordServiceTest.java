package com.woohaengshi.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.StudySubject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowDailyRecordResponse;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;
import com.woohaengshi.backend.dto.response.studyrecord.ShowYearlyRecordResponse;
import com.woohaengshi.backend.dto.result.MonthlyTotalRecordResult;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
import com.woohaengshi.backend.repository.StudySubjectRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.service.studyrecord.StudyRecordServiceImpl;
import com.woohaengshi.backend.support.fixture.MemberFixture;
import com.woohaengshi.backend.support.fixture.StudyRecordFixture;

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
    @InjectMocks private StudyRecordServiceImpl studyRecordService;

    @Test
    void 첫_공부_기록을_저장할_수_있다() {
        Member member = MemberFixture.builder().build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));
        StudyRecord studyRecord = StudyRecordFixture.from(request, 1L);

        given(memberRepository.existsById(member.getId())).willReturn(true);
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
                () -> verify(studySubjectRepository, times(2)).save(any(StudySubject.class)));
    }

    @Test
    void 이미_공부한_과목의_기록을_누적해_저장할_수_있다() {
        Member member = MemberFixture.builder().id(1L).build();
        StudyRecord existStudyRecord =
                StudyRecordFixture.builder().member(member).id(1L).time(20).build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));

        given(memberRepository.existsById(member.getId())).willReturn(true);
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
                () -> verify(studySubjectRepository, never()).save(any(StudySubject.class)));
    }

    @Test
    void 처음_공부한_과목의_기록을_누적해_저장할_수_있다() {
        Member member = MemberFixture.builder().id(1L).build();
        StudyRecord existStudyRecord =
                StudyRecordFixture.builder().member(member).id(1L).time(20).build();
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));

        given(memberRepository.existsById(member.getId())).willReturn(true);
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
                () -> verify(studySubjectRepository, times(2)).save(any(StudySubject.class)));
    }

    @Test
    void 현재_연도와_월을_통해_공부_기록을_조회_한다() {
        Member member = MemberFixture.builder().build();
        List<Object[]> records = new ArrayList<>();
        records.add(new Object[] {1, 36000, 2L, "CSS"});
        records.add(new Object[] {1, 36000, 1L, "HTML"});
        records.add(new Object[] {6, 58000, 3L, "JS"});
        records.add(new Object[] {9, 47000, 3L, "JS"});
        records.add(new Object[] {9, 47000, 2L, "CSS"});
        YearMonth date = YearMonth.now();

        ShowMonthlyRecordResponse expected = ShowMonthlyRecordResponse.of(date, records);

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(studyRecordRepository.findByYearAndMonthAndMemberId(2024, 8, member.getId()))
                .willReturn(records);

        ShowMonthlyRecordResponse response =
                studyRecordService.showMonthlyRecord(date, member.getId());

        assertAll(
                "response",
                () -> assertThat(response.getYear()).isEqualTo(expected.getYear()),
                () -> assertThat(response.getMonth()).isEqualTo(expected.getMonth()),
                () -> {
                    for (int i = 0; i < response.getDaily().size(); i++) {
                        ShowDailyRecordResponse daily = response.getDaily().get(i);
                        ShowDailyRecordResponse expectedDaily = expected.getDaily().get(i);

                        assertThat(daily.getDay()).isEqualTo(expectedDaily.getDay());
                        assertThat(daily.getTime()).isEqualTo(expectedDaily.getTime());

                        for (int j = 0; j < daily.getSubjects().size(); j++) {
                            assertThat(daily.getSubjects().get(j).getId())
                                    .isEqualTo(expectedDaily.getSubjects().get(j).getId());
                            assertThat(daily.getSubjects().get(j).getName())
                                    .isEqualTo(expectedDaily.getSubjects().get(j).getName());
                        }
                    }
                });
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
                    for (int i = 0; i < response.getMonthly().size(); i++) {
                        assertThat(response.getMonthly().get(i).getMonth())
                                .isEqualTo(expected.get(i).getMonth());
                        assertThat(response.getMonthly().get(i).getTotal())
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
}
