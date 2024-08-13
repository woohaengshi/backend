package com.woohaengshi.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.StudySubject;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.support.RepositoryTest;
import com.woohaengshi.backend.support.fixture.MemberFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@RepositoryTest
public class StudyRecordRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private StudySubjectRepository studySubjectRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private StudyRecordRepository studyRecordRepository;

    private StudyRecord 저장(StudyRecord studyRecord) {
        return studyRecordRepository.save(studyRecord);
    }

    private StudySubject 저장(StudySubject studySubject) {
        return studySubjectRepository.save(studySubject);
    }

    private Member 저장(Member member) {
        return memberRepository.save(member);
    }

    private Subject 저장(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Test
    @DisplayName("1월과 2월의 공부 기록이 존재할 때 1월의 공부 기록만 제대로 조회가 되는지 확인")
    public void findStudyRecords() {
        Member member = 저장(MemberFixture.builder().build());
        LocalDate date1 = LocalDate.of(2024, 1, 10);
        LocalDate date2 = LocalDate.of(2024, 2, 11);
        StudyRecord studyRecord1 =
                저장(StudyRecord.builder().member(member).time(500).date(date1).build());
        StudyRecord studyRecord2 =
                저장(StudyRecord.builder().member(member).time(400).date(date2).build());
        Subject subject = 저장(Subject.builder().name("HTML").member(member).build());
        StudySubject studySubject1 =
                저장(StudySubject.builder().studyRecord(studyRecord1).subject(subject).build());
        StudySubject studySubject2 =
                저장(StudySubject.builder().studyRecord(studyRecord2).subject(subject).build());

        List<Object[]> result1 =
                studyRecordRepository.findByYearAndMonthAndMemberId(2024, 1, member.getId());

        assertAll(
                "response",
                () -> assertThat(result1.size()).isEqualTo(1),
                () ->
                        assertThat(((Number) result1.get(0)[0]).intValue())
                                .isEqualTo(date1.getDayOfMonth()),
                () ->
                        assertThat(((Number) result1.get(0)[1]).intValue())
                                .isEqualTo(studyRecord1.getTime()),
                () -> assertThat((Long) result1.get(0)[2]).isEqualTo(subject.getId()),
                () -> assertThat((String) result1.get(0)[3]).isEqualTo(subject.getName()));
    }
}
