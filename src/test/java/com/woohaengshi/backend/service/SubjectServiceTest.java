package com.woohaengshi.backend.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.dto.response.studyrecord.ShowTimerResponse;
import com.woohaengshi.backend.dto.response.subject.ShowSubjectsResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.StudyRecordRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.service.subject.SubjectServiceImpl;
import com.woohaengshi.backend.support.fixture.MemberFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {
    @Mock private MemberRepository memberRepository;
    @Mock private StudyRecordRepository studyRecordRepository;
    @Mock private SubjectRepository subjectRepository;
    @InjectMocks private SubjectServiceImpl subjectService;

    @Test
    void 타이머를_조회_한다() {
        Member member = MemberFixture.builder().build();
        LocalDate todayDate = LocalDate.of(2024, 8, 9);
        int todayStudyTime = 30;
        Subject subject = Subject.builder().id(1L).name("HTML").member(member).build();
        ShowSubjectsResponse subjectResponse =
                ShowSubjectsResponse.of(subject.getId(), subject.getName());

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(subjectRepository.findAllByMemberIdOrderByNameAsc(member.getId()))
                .willReturn(Stream.of(subject));
        given(studyRecordRepository.findByDateAndMemberId(todayDate, member.getId()))
                .willReturn(Optional.of(new StudyRecord(1L, todayStudyTime, todayDate, null)));

        ShowTimerResponse response = subjectService.getTimer(member.getId());

        List<ShowSubjectsResponse> expectedSubjects = List.of(subjectResponse);
        List<ShowSubjectsResponse> actualSubjects = response.getSubjects();

        assertAll(
                "response",
                () -> assertThat(response.getTime()).isEqualTo(todayStudyTime),
                () -> assertTrue(expectedSubjects.size() == actualSubjects.size()),
                () ->
                        assertThat(actualSubjects.get(0).getId())
                                .isEqualTo(expectedSubjects.get(0).getId()),
                () ->
                        assertThat(actualSubjects.get(0).getName())
                                .isEqualTo(expectedSubjects.get(0).getName()));
    }

    @Test
    void 타이머를_처음_조회_한다() {
        Member member = MemberFixture.builder().build();
        LocalDate todayDate = LocalDate.of(2024, 8, 9);
        int todayStudyTime = 0;

        Subject subject = Subject.builder().id(1L).name("HTML").member(member).build();
        ShowSubjectsResponse subjectResponse =
                ShowSubjectsResponse.of(subject.getId(), subject.getName());

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(subjectRepository.findAllByMemberIdOrderByNameAsc(member.getId()))
                .willReturn(Stream.of(subject));
        given(studyRecordRepository.findByDateAndMemberId(todayDate, member.getId()))
                .willReturn(Optional.of(new StudyRecord(1L, todayStudyTime, todayDate, null)));

        ShowTimerResponse response = subjectService.getTimer(member.getId());

        List<ShowSubjectsResponse> expectedSubjects = List.of(subjectResponse);
        List<ShowSubjectsResponse> actualSubjects = response.getSubjects();

        assertAll(
                "response",
                () -> assertThat(response.getTime()).isEqualTo(todayStudyTime),
                () -> assertTrue(expectedSubjects.size() == actualSubjects.size()),
                () ->
                        assertThat(actualSubjects.get(0).getId())
                                .isEqualTo(expectedSubjects.get(0).getId()),
                () ->
                        assertThat(actualSubjects.get(0).getName())
                                .isEqualTo(expectedSubjects.get(0).getName()));
    }

    @Test
    void 회원이_존재하지_않으면_예외를_던진다() {
        Member member = MemberFixture.builder().build();

        given(memberRepository.existsById(member.getId())).willReturn(false);

        WoohaengshiException thrown =
                assertThrows(
                        WoohaengshiException.class, () -> subjectService.getTimer(member.getId()));
        assertAll(
                "exception",
                () ->
                        assertEquals(
                                ErrorCode.MEMBER_NOT_FOUND,
                                thrown.getErrorCode(),
                                "Error code should match"));
    }
}
