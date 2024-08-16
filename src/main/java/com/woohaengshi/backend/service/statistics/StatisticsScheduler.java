package com.woohaengshi.backend.service.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsScheduler {

    private final StatisticsService statisticsService;

    @Scheduled(cron = "0 0 5 1 * ?", zone = "Asia/Seoul")
    private void initMonthlyStatistics() {

    }

    @Scheduled(cron = "0 0 5 * * MON", zone = "Asia/Seoul")
    private void initWeeklyStatistics() {

    }

}
