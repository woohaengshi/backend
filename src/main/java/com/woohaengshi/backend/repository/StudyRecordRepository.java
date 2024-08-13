package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.StudyRecord;
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
            value =
                    "SELECT DAY(r.date), r.time, sb.id, sb.name\n"
                            + "FROM study_record r\n"
                            + "INNER JOIN study_subject st ON r.id = st.study_record_id\n"
                            + "INNER JOIN subject sb ON st.subject_id = sb.id\n"
                            + "WHERE YEAR(r.date) = :year\n"
                            + "  AND MONTH(r.date) = :month\n"
                            + "  AND r.member_id = :memberId\n"
                            + "ORDER BY r.date ASC;",
            nativeQuery = true)
    List<Object[]> findByYearAndMonthAndMemberId(
            @Param(value = "year") int year,
            @Param(value = "month") int month,
            @Param(value = "memberId") Long memberId);

    @Query("SELECT COUNT(s) + 1 FROM StudyRecord s WHERE s.date = :date AND s.time > :time")
    Integer findRankByDateAndMemberId(LocalDate date, int time);

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
