package com.woohaengshi.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowDailyRecordResponse;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StudyRecordServiceTest {
    @Mock private MemberRepository memberRepository;
    @Mock private StudyRecordRepository studyRecordRepository;
    @Mock private SubjectRepository subjectRepository;
    @InjectMocks private StudyRecordServiceImpl studyRecordService;

    @Test
    void 첫_공부_기록을_저장할_수_있다() {
        Member member = MemberFixture.builder().build();
        SaveRecordRequest request =
                new SaveRecordRequest(LocalDate.now(), 10, List.of("HTML", "CSS"));
        StudyRecord studyRecord = request.toStudyRecord(member);
        given(studyRecordRepository.findByDateAndMemberId(request.getDate(), member.getId()))
                .willReturn(Optional.empty());
        given(studyRecordRepository.save(any(StudyRecord.class))).willReturn(studyRecord);
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        request.getSubjects()
                .forEach(
                        subject -> {
                            given(
                                            subjectRepository.existsByNameAndStudyRecordId(
                                                    subject, studyRecord.getId()))
                                    .willReturn(false);
                        });

        assertAll(
                () -> studyRecordService.save(request, member.getId()),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(request.getDate(), member.getId()),
                () -> verify(memberRepository, times(1)).findById(member.getId()),
                () -> verify(studyRecordRepository, times(1)).save(any(StudyRecord.class)),
                () ->
                        verify(subjectRepository, times(2))
                                .existsByNameAndStudyRecordId(
                                        any(String.class), eq(studyRecord.getId())));
    }

    @Test
    void 공부_기록을_누적해_저장할_수_있다() {
        Member member = MemberFixture.builder().id(1L).build();
        StudyRecord existStudyRecord = StudyRecordFixture.builder().member(member).time(20).build();
        SaveRecordRequest request =
                new SaveRecordRequest(LocalDate.now(), 10, List.of("HTML", "CSS"));
        StudyRecord newStudyRecord = request.toStudyRecord(member);
        given(studyRecordRepository.findByDateAndMemberId(request.getDate(), member.getId()))
                .willReturn(Optional.of(existStudyRecord));
        request.getSubjects()
                .forEach(
                        subject -> {
                            given(
                                            subjectRepository.existsByNameAndStudyRecordId(
                                                    subject, newStudyRecord.getId()))
                                    .willReturn(false);
                        });
        assertAll(
                () -> studyRecordService.save(request, member.getId()),
                () -> assertThat(existStudyRecord.getTime()).isEqualTo(30),
                () ->
                        verify(studyRecordRepository, times(1))
                                .findByDateAndMemberId(request.getDate(), member.getId()),
                () ->
                        verify(subjectRepository, times(2))
                                .existsByNameAndStudyRecordId(
                                        any(String.class), eq(newStudyRecord.getId())));
    }

    @Test
    void 이미_학습한_과목일_경우_저장되지_않는다() {
        String DUPLICATED_SUBJECT = "HTML";
        Member member = MemberFixture.builder().id(1L).build();
        StudyRecord EXIST_RECORD = StudyRecordFixture.builder().member(member).time(20).build();
        SaveRecordRequest request =
                new SaveRecordRequest(LocalDate.now(), 10, List.of(DUPLICATED_SUBJECT));
        StudyRecord newStudyRecord = request.toStudyRecord(member);
        given(studyRecordRepository.findByDateAndMemberId(request.getDate(), member.getId()))
                .willReturn(Optional.of(EXIST_RECORD));
        given(
                        subjectRepository.existsByNameAndStudyRecordId(
                                DUPLICATED_SUBJECT, newStudyRecord.getId()))
                .willReturn(true);
        studyRecordService.save(request, member.getId());
        assertAll(() -> verify(subjectRepository, never()).save(any(Subject.class)));
    }

    @Test
    void 연도와_월을_통해_공부_기록을_조회_한다() {
        Member member = MemberFixture.builder().build();
        List<Object[]> records = new ArrayList<>();
        records.add(new Object[] {1, 36000, 2L, "CSS"});
        records.add(new Object[] {1, 36000, 1L, "HTML"});
        records.add(new Object[] {6, 58000, 3L, "JS"});
        records.add(new Object[] {9, 47000, 3L, "JS"});
        records.add(new Object[] {9, 47000, 2L, "CSS"});

        ShowMonthlyRecordResponse expected = ShowMonthlyRecordResponse.of(2024, 8, records);

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(studyRecordRepository.findByYearAndMonthAndMemberId(2024, 8, member.getId()))
                .willReturn(records);

        ShowMonthlyRecordResponse response =
                studyRecordService.showMonthlyRecord(
                        expected.getYear(), expected.getMonth(), member.getId());

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
    void 기록이_없는_월을_조회_한다() {
        Member member = MemberFixture.builder().build();
        List<Object[]> records = new ArrayList<>();

        ShowMonthlyRecordResponse expected = ShowMonthlyRecordResponse.of(2024, 8, records);

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(studyRecordRepository.findByYearAndMonthAndMemberId(2024, 8, member.getId()))
                .willReturn(records);

        ShowMonthlyRecordResponse response =
                studyRecordService.showMonthlyRecord(
                        expected.getYear(), expected.getMonth(), member.getId());

        assertAll(
                "response",
                () -> assertThat(response.getYear()).isEqualTo(expected.getYear()),
                () -> assertThat(response.getMonth()).isEqualTo(expected.getMonth()),
                () -> assertThat(response.getDaily()).isEqualTo(expected.getDaily()),
                () -> assertThat(response.getDaily()).isNotNull()
        );
    }

    @Test
    void 회원이_존재하지_않을_경우_예외를_던진다() {
        Member member = MemberFixture.builder().build();
        SaveRecordRequest request =
                new SaveRecordRequest(LocalDate.now(), 10, List.of("HTML", "CSS"));
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
