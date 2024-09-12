package com.woohaengshi.backend.service.statistics;

import com.woohaengshi.backend.log.StatisticsLog;
import com.woohaengshi.backend.repository.StatisticsRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StatisticsScheduler {
    private final StatisticsRepository statisticsRepository;
    private final StatisticsLog statisticsLog;

    @Scheduled(cron = "0 0 5 1 * ?", zone = "Asia/Seoul")
    @Transactional
    public void initMonthlyStatistics() {
        statisticsRepository.initMonthlyTime();
    }

    @Scheduled(cron = "0 0 5 * * MON", zone = "Asia/Seoul")
    @Transactional
    public void initWeeklyStatistics() {
        statisticsLog.printWeeklyRanking();
        statisticsRepository.initWeeklyTime();
    }
}
