package com.woohaengshi.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.StudySubject;
import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.domain.member.Course;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.member.State;
import com.woohaengshi.backend.support.RepositoryTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RepositoryTest
public class StudyRecordRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private StudySubjectRepository studySubjectRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private StudyRecordRepository studyRecordRepository;

    @BeforeEach
    public void setUp() {
        Member member =
                Member.builder()
                        .id(100L)
                        .password("pwd")
                        .course(Course.CLOUD_SERVICE)
                        .email("dd@Naver")
                        .name("si")
                        .state(State.ACTIVE)
                        .sleepDate(LocalDate.now())
                        .createdAt(LocalDateTime.now())
                        .build();
        memberRepository.save(member);

        LocalDate date1 = LocalDate.of(2024, 8, 10);
        LocalDate date2 = LocalDate.of(2024, 8, 11);

        StudyRecord studyRecord1 =
                StudyRecord.builder().member(member).time(500).date(date1).build();
        StudyRecord studyRecord2 =
                StudyRecord.builder().member(member).time(600).date(date1).build();
        StudyRecord studyRecord3 =
                StudyRecord.builder().member(member).time(700).date(date1).build();
        StudyRecord studyRecord4 =
                StudyRecord.builder().member(member).time(300).date(date2).build();
        StudyRecord studyRecord5 =
                StudyRecord.builder().member(member).time(300).date(date2).build();
        studyRecordRepository.save(studyRecord1);
        studyRecordRepository.save(studyRecord2);
        studyRecordRepository.save(studyRecord3);
        studyRecordRepository.save(studyRecord4);
        studyRecordRepository.save(studyRecord5);

        Subject subject1 = Subject.builder().name("HTML").member(member).build();
        Subject subject2 = Subject.builder().name("CSS").member(member).build();
        Subject subject3 = Subject.builder().name("JS").member(member).build();
        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        subjectRepository.save(subject3);

        StudySubject studySubject1 =
                StudySubject.builder().studyRecord(studyRecord1).subject(subject1).build();
        StudySubject studySubject2 =
                StudySubject.builder().studyRecord(studyRecord2).subject(subject2).build();
        StudySubject studySubject3 =
                StudySubject.builder().studyRecord(studyRecord3).subject(subject3).build();
        StudySubject studySubject4 =
                StudySubject.builder().studyRecord(studyRecord4).subject(subject2).build();
        StudySubject studySubject5 =
                StudySubject.builder().studyRecord(studyRecord5).subject(subject3).build();
        studySubjectRepository.save(studySubject1);
        studySubjectRepository.save(studySubject2);
        studySubjectRepository.save(studySubject3);
        studySubjectRepository.save(studySubject4);
        studySubjectRepository.save(studySubject5);
    }

    @Test
    @DisplayName("공부 기록이 일 별로 제대로 조회가 되는지 확인")
    public void findStudyRecords() {
        List<Object[]> result = studyRecordRepository.findByYearAndMonthAndMemberId(2024, 8, 100L);
        List<Object[]> expected = new ArrayList<>();
        expected.add(new Object[] {10, 500, 1L, "HTML"});
        expected.add(new Object[] {10, 600, 1L, "CSS"});
        expected.add(new Object[] {10, 700, 1L, "JS"});
        expected.add(new Object[] {11, 300, 1L, "CSS"});
        expected.add(new Object[] {11, 300, 1L, "JS"});

        assertAll(
                "response",
                () -> assertThat(result.size()).isEqualTo(expected.size()),
                () -> {
                    for (int i = 0; i < result.size(); i++) {
                        assertThat(result.get(i)[0]).isEqualTo(expected.get(i)[0]);
                        assertThat(result.get(i)[1]).isEqualTo(expected.get(i)[1]);
                        assertThat(result.get(i)[3]).isEqualTo(expected.get(i)[3]);
                    }
                });
    }
}
