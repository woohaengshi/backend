package com.woohaengshi.backend.service;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {
    @Mock private MemberRepository memberRepository;
    @Mock private StudyRecordRepository studyRecordRepository;
    @Mock private SubjectRepository subjectRepository;

    @Test
    void 타이머를_조회_한다() {
        Member member = MemberFixture.builder().build();
        LocalDate todayDate = LocalDate.of(2024, 8, 9);
        int todayStudyTime = 30;
        Subject subject = Subject.builder().id(1L).name("HTML").member(member).build();
        ShowSubjectsResponse subjectResponse =
                new ShowSubjectsResponse(subject.getId(), subject.getName());

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(subjectRepository.findAllByMemberIdOrderByNameAsc(member.getId()))
                .willReturn(Stream.of(subject));
        given(studyRecordRepository.findByDateAndMemberId(todayDate, member.getId()))
                .willReturn(Optional.of(new StudyRecord(1L, todayStudyTime, todayDate, null)));

        SubjectServiceImpl subjectService =
                new SubjectServiceImpl(studyRecordRepository, subjectRepository, memberRepository);

        ShowTimerResponse response = subjectService.getTimer(member.getId());

        List<ShowSubjectsResponse> expectedSubjects = Collections.singletonList(subjectResponse);
        List<ShowSubjectsResponse> actualSubjects = response.getSubjects();

        assertAll(
                "response",
                () -> assertEquals(todayStudyTime, response.getTime(), "Study time should match"),
                () ->
                        assertTrue(
                                expectedSubjects.size() == actualSubjects.size()
                                        && expectedSubjects
                                                .get(0)
                                                .getId()
                                                .equals(actualSubjects.get(0).getId())
                                        && expectedSubjects
                                                .get(0)
                                                .getName()
                                                .equals(actualSubjects.get(0).getName()),
                                "Subjects list should match"));
    }

    @Test
    void 타이머를_처음_조회_한다() {
        Member member = MemberFixture.builder().build();
        LocalDate todayDate = LocalDate.of(2024, 8, 9);
        Subject subject = Subject.builder().id(1L).name("HTML").member(member).build();
        ShowSubjectsResponse subjectResponse =
                new ShowSubjectsResponse(subject.getId(), subject.getName());

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(subjectRepository.findAllByMemberIdOrderByNameAsc(member.getId()))
                .willReturn(Stream.of(subject));

        SubjectServiceImpl subjectService =
                new SubjectServiceImpl(studyRecordRepository, subjectRepository, memberRepository);

        ShowTimerResponse response = subjectService.getTimer(member.getId());

        List<ShowSubjectsResponse> expectedSubjects = Collections.singletonList(subjectResponse);
        List<ShowSubjectsResponse> actualSubjects = response.getSubjects();

        assertAll(
                "response",
                () -> assertEquals(0, response.getTime(), "Study time should match"),
                () ->
                        assertTrue(
                                expectedSubjects.size() == actualSubjects.size()
                                        && expectedSubjects
                                                .get(0)
                                                .getId()
                                                .equals(actualSubjects.get(0).getId())
                                        && expectedSubjects
                                                .get(0)
                                                .getName()
                                                .equals(actualSubjects.get(0).getName()),
                                "Subjects list should match"));
    }

    @Test
    void 회원이_존재하지_않으면_예외를_던진다() {
        Member member = MemberFixture.builder().build();

        given(memberRepository.existsById(member.getId())).willReturn(false);

        SubjectServiceImpl subjectService =
                new SubjectServiceImpl(studyRecordRepository, subjectRepository, memberRepository);

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
