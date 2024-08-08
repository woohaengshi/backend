package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.StatisticsReadDto;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.service.statistics.StatisticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StatisticsController {

    private final StatisticsQueryService statisticsQueryService;

    @GetMapping("/rank")
    public StatisticsReadDto getRanking(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "type", defaultValue = "WEEKLY") String type,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        if (page < 0) throw new WoohaengshiException(ErrorCode._PAGE_OVER_RANGE);
        if (size <= 0) throw new WoohaengshiException(ErrorCode._PAGE_SIZE_MIN);
        Pageable pageable = PageRequest.of(page, size);
        StatisticsType statisticsType = StatisticsType.fromString(type);

        return statisticsQueryService.getRankingDataWithMember(1, statisticsType, pageable);
    }
}
