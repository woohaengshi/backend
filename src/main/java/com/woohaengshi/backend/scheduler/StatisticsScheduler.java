package com.woohaengshi.backend.scheduler;

import com.woohaengshi.backend.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsScheduler {
    private final StatisticsService statisticsService;
}
