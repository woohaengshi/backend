package com.woohaengshi.backend.service;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class StudyRecordServiceTest {
    @Mock private MemberRepository memberRepository;
    @Mock private StudyRecordRepository studyRecordRepository;
    @Mock private SubjectRepository subjectRepository;
    @InjectMocks private StudyRecordService studyRecordService;

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
}
