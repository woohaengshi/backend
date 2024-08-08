package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.statistics.Statistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long>, JpaSpecificationExecutor<Statistics> {

    Optional<Statistics> findByMemberId(Long memberId);
}

