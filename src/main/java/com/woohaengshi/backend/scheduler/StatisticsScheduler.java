package com.woohaengshi.backend.scheduler;

import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsScheduler {
    private final StatisticsService statisticsService;

//    @Scheduled(fixedDelay = 2000)
    @Scheduled(cron = "0 0 5 * * ?")
    public void UpdateWeekly() {
        statisticsService.updateStatisticsTime(StatisticsType.WEEKLY);
    }
}
