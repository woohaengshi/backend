package com.woohaengshi.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.domain.StudySubject;
import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.result.MonthlyTotalRecordResult;
import com.woohaengshi.backend.dto.result.ShowCalendarResult;
import com.woohaengshi.backend.repository.studyrecord.StudyRecordRepository;
import com.woohaengshi.backend.support.RepositoryTest;
import com.woohaengshi.backend.support.fixture.MemberFixture;
import com.woohaengshi.backend.support.fixture.StatisticsFixture;
import com.woohaengshi.backend.support.fixture.SubjectFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @DisplayName("2024년의 공부 기록을 조회하면 월 별로 총 공부 시간이 제대로 조회되는지 확인")
    public void findYearlyRecords() {
        Member member = 저장(MemberFixture.builder().build());
        LocalDate date1 = LocalDate.of(2024, 1, 10);
        LocalDate date2 = LocalDate.of(2024, 2, 11);
        LocalDate date3 = LocalDate.of(2024, 2, 20);
        StudyRecord studyRecord1 =
                저장(StudyRecord.builder().member(member).time(500).date(date1).build());
        StudyRecord studyRecord2 =
                저장(StudyRecord.builder().member(member).time(400).date(date2).build());
        StudyRecord studyRecord3 =
                저장(StudyRecord.builder().member(member).time(1000).date(date3).build());

        List<MonthlyTotalRecordResult> results =
                studyRecordRepository.findMonthlyTotalByYearAndMemberId(2024, member.getId());

        assertAll(
                "response",
                () -> assertThat(results.size()).isEqualTo(2),
                () -> assertThat(results.get(0).getMonth()).isEqualTo(date1.getMonth().getValue()),
                () -> assertThat(results.get(0).getTotal()).isEqualTo(studyRecord1.getTime()),
                () -> assertThat(results.get(1).getMonth()).isEqualTo(date2.getMonth().getValue()),
                () ->
                        assertThat(results.get(1).getTotal())
                                .isEqualTo(studyRecord2.getTime() + studyRecord3.getTime()));
    }

    @Test
    void 공부_기록을_월_단위로_조회할_수_있다() {
        Member member = 저장(MemberFixture.builder().build());
        LocalDate date1 = LocalDate.of(2024, 1, 10);
        LocalDate date2 = LocalDate.of(2024, 2, 11);
        LocalDate date3 = LocalDate.of(2024, 2, 20);

        저장(StudyRecord.builder().member(member).time(400).date(date1).build());
        StudyRecord studyRecord =
                저장(StudyRecord.builder().member(member).time(500).date(date2).build());
        저장(StudyRecord.builder().member(member).time(1000).date(date3).build());
        Subject subject = 저장(SubjectFixture.builder().member(member).build());
        저장(StudySubject.builder().studyRecord(studyRecord).subject(subject).build());

        List<ShowCalendarResult> result =
                studyRecordRepository.findStudyRecordInCalendar(2024, 2, member.getId());
        assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getSubjects().size()).isEqualTo(1),
                () -> assertThat(result.get(0).getTime()).isEqualTo(studyRecord.getTime()),
                () -> assertThat(result.get(1).getSubjects().size()).isEqualTo(0));
    }

    @Test
    void 일간_시간의_멤버들을_정렬해서_찾을_수_있다(){
        Member member = 저장(MemberFixture.builder().build());

        StudyRecord studyRecord1 =
                저장(StudyRecord.builder().member(member).time(500).date(LocalDate.now()).build());
        StudyRecord studyRecord2 =
                저장(StudyRecord.builder().member(member).time(400).date(LocalDate.now()).build());
        StudyRecord studyRecord3 =
                저장(StudyRecord.builder().member(member).time(1000).date(LocalDate.now()).build());

        Pageable pageable = PageRequest.of(0, 10);

        List<StudyRecord> studyRecordList = studyRecordRepository.findStudyRecordsByDateSortedByTimeDesc(LocalDate.now(), pageable);

        assertThat(studyRecordList.get(0).getId()).isEqualTo(studyRecord3.getId());
        assertThat(studyRecordList.get(1).getId()).isEqualTo(studyRecord1.getId());
        assertThat(studyRecordList.get(2).getId()).isEqualTo(studyRecord2.getId());
    }

    @Test
    void 주간_월간대_별의_시간의_통계_개수를_구할_수_있다(){
        Member member = 저장(MemberFixture.builder().build());

        StudyRecord studyRecord1 =
                저장(StudyRecord.builder().member(member).time(500).date(LocalDate.now()).build());
        StudyRecord studyRecord2 =
                저장(StudyRecord.builder().member(member).time(400).date(LocalDate.now()).build());
        StudyRecord studyRecord3 =
                저장(StudyRecord.builder().member(member).time(1000).date(LocalDate.now()).build());

        long count = studyRecordRepository.getCountStudyRecordsByDate(LocalDate.now());

        assertThat(count).isEqualTo(3);
    }

}
