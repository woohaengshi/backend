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
import com.woohaengshi.backend.domain.StudySubject;
import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
import com.woohaengshi.backend.repository.StudySubjectRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.support.fixture.MemberFixture;
import com.woohaengshi.backend.support.fixture.StudyRecordFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StudyRecordServiceTest {
    @Mock private MemberRepository memberRepository;
    @Mock private StudyRecordRepository studyRecordRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private StudySubjectRepository studySubjectRepository;
    @InjectMocks private StudyRecordService studyRecordService;

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
