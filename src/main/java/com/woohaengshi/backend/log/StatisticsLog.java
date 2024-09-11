package com.woohaengshi.backend.log;

import com.woohaengshi.backend.domain.statistics.Statistics;
import com.woohaengshi.backend.repository.statistics.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Slf4j
@Component
public class StatisticsLog {
    private final StatisticsRepository statisticsRepository;

    public void printWeeklyRanking(){
        List<Statistics> statisticsList = statisticsRepository.findAllByOrderByWeeklyTimeDesc();
        IntStream.range(0, statisticsList.size()).forEach(idx -> {
            Statistics item = statisticsList.get(idx);
            log.info("name = {}, rank = {}, weeklyTime = {}",
                    item.getMember().getName(),
                    idx + 1,
                    item.getWeeklyTime());
        });
    }
}
