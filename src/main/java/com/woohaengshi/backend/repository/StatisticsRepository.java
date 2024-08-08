package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.statistics.Statistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query("SELECT s FROM Statistics s WHERE s.member.id = :memberId")
    Optional<Statistics> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(s) FROM Statistics s WHERE s.dailyTime > :dailyTime")
    int checkByDailyTimeRanking(@Param("dailyTime") int dailyTime);

    @Query("SELECT COUNT(s) FROM Statistics s WHERE s.weeklyTime > :weeklyTime")
    int checkByWeeklyTimeRanking(@Param("weeklyTime") int weeklyTime);

    @Query("SELECT COUNT(s) FROM Statistics s WHERE s.monthlyTime > :monthlyTime")
    int checkByMonthlyTimeRanking(@Param("monthlyTime") int monthlyTime);

    @Query("SELECT s FROM Statistics s ORDER BY s.weeklyTime DESC")
    Slice<Statistics> findAllByWeeklyTimeRanking(Pageable pageable);

    @Query("SELECT s FROM Statistics s ORDER BY s.dailyTime DESC")
    Slice<Statistics> findAllByDailyTimeTimeRanking(Pageable pageable);

    @Query("SELECT s FROM Statistics s ORDER BY s.monthlyTime DESC")
    Slice<Statistics> findAllByMonthlyTimeRanking(Pageable pageable);
}
