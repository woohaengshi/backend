package com.woohaengshi.backend.scheduler;

import com.woohaengshi.backend.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsScheduler {
    private final StatisticsService statisticsService;

    @Scheduled(cron = "0 0 5 * * ?")
    public void task(){
    }
}
