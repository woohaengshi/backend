package com.woohaengshi.backend.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.response.timer.ShowTimerResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.repository.MemberRepository;
import com.woohaengshi.backend.repository.SubjectRepository;
import com.woohaengshi.backend.repository.studyrecord.StudyRecordRepository;
import com.woohaengshi.backend.service.timer.TimerServiceImpl;
import com.woohaengshi.backend.support.fixture.MemberFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TimerServiceTest {
    @Mock private MemberRepository memberRepository;
    @Mock private StudyRecordRepository studyRecordRepository;
    @Mock private SubjectRepository subjectRepository;
    @InjectMocks private TimerServiceImpl subjectService;

    @Test
    void 타이머를_조회_한다() {
        Member member = MemberFixture.builder().build();
        Subject subject1 = Subject.builder().id(1L).name("CSS").member(member).build();
        Subject subject2 = Subject.builder().id(2L).name("HTML").member(member).build();
        List<Subject> subjects = List.of(subject1, subject2);

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(subjectRepository.findAllByMemberId(member.getId())).willReturn(subjects);
        given(studyRecordRepository.findByDateAndMemberId(LocalDate.now(), member.getId()))
                .willReturn(Optional.of(new StudyRecord(1L, 30, LocalDate.now(), member)));

        ShowTimerResponse response = subjectService.getTimer(member.getId());

        assertAll(
                "response",
                () -> assertThat(response.getTime()).isEqualTo(30),
                () -> assertTrue(response.getSubjects().size() == subjects.size()),
                () -> {
                    for (int i = 0; i < subjects.size(); i++) {
                        assertThat(response.getSubjects().get(i).getId())
                                .isEqualTo(subjects.get(i).getId());
                        assertThat(response.getSubjects().get(i).getName())
                                .isEqualTo(subjects.get(i).getName());
                    }
                });
    }

    @Test
    void 오늘의_공부_기록이_없는_경우에_타이머를_조회_한다() {
        Member member = MemberFixture.builder().build();
        Subject subject1 = Subject.builder().id(1L).name("CSS").member(member).build();
        Subject subject2 = Subject.builder().id(2L).name("HTML").member(member).build();
        List<Subject> subjects = List.of(subject1, subject2);

        given(memberRepository.existsById(member.getId())).willReturn(true);
        given(subjectRepository.findAllByMemberId(member.getId())).willReturn(subjects);
        given(studyRecordRepository.findByDateAndMemberId(LocalDate.now(), member.getId()))
                .willReturn(Optional.empty());

        ShowTimerResponse response = subjectService.getTimer(member.getId());

        assertAll(
                "response",
                () -> assertThat(response.getTime()).isEqualTo(0),
                () -> assertTrue(response.getSubjects().size() == subjects.size()),
                () -> {
                    for (int i = 0; i < subjects.size(); i++) {
                        assertThat(response.getSubjects().get(i).getId())
                                .isEqualTo(subjects.get(i).getId());
                        assertThat(response.getSubjects().get(i).getName())
                                .isEqualTo(subjects.get(i).getName());
                    }
                });
    }

    @Test
    void 회원이_존재하지_않으면_예외를_던진다() {
        given(memberRepository.existsById(10L)).willReturn(false);

        WoohaengshiException thrown =
                assertThrows(WoohaengshiException.class, () -> subjectService.getTimer(10L));
        assertAll(
                "exception", () -> assertEquals(ErrorCode.MEMBER_NOT_FOUND, thrown.getErrorCode()));
    }
}
