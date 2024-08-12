package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.statistics.Statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepository
        extends JpaRepository<Statistics, Long>, JpaSpecificationExecutor<Statistics> {

    @Query("SELECT s FROM Statistics s JOIN FETCH s.member WHERE s.member.id = :memberId")
    Optional<Statistics> findByMemberId(Long memberId);

    @Query("SELECT s FROM Statistics s JOIN FETCH s.member")
    List<Statistics> findAllWithMember();
}
