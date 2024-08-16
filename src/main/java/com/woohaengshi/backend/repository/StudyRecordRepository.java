package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.StudyRecord;
import com.woohaengshi.backend.dto.result.DailyStudyRecordResult;
import com.woohaengshi.backend.dto.result.MonthlyTotalRecordResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyRecordRepository
        extends JpaRepository<StudyRecord, Long>, JpaSpecificationExecutor<StudyRecord> {
    Optional<StudyRecord> findByDateAndMemberId(LocalDate date, Long memberId);

    @Query(
            "select new com.woohaengshi.backend.dto.result.DailyStudyRecordResult("
                    + "DAY(sr.date), sr.time, sb.id, sb.name) "
                    + "FROM StudyRecord sr "
                    + "JOIN sr.studySubjects ss "
                    + "JOIN ss.subject sb "
                    + "WHERE YEAR(sr.date) = :year "
                    + "AND MONTH(sr.date) = :month "
                    + "AND sr.member.id = :memberId "
                    + "ORDER BY sr.date, sr.id")
    List<DailyStudyRecordResult> findByYearAndMonthAndMemberId(
            @Param("year") int year, @Param("month") int month, @Param("memberId") Long memberId);

    @Query("SELECT COUNT(s) + 1 FROM StudyRecord s WHERE s.date = :date AND s.time > :time")
    Integer findRankByDate(LocalDate date, int time);

    @Query(
            "select new com.woohaengshi.backend.dto.result.MonthlyTotalRecordResult("
                    + "MONTH(sr.date), SUM(sr.time)) "
                    + "from StudyRecord sr "
                    + "where YEAR(sr.date) = :year "
                    + "and sr.member.id = :memberId "
                    + "group by MONTH(sr.date) "
                    + "order by MONTH(sr.date)")
    List<MonthlyTotalRecordResult> findMonthlyTotalByYearAndMemberId(
            @Param("year") int year, @Param("memberId") Long memberId);
}
