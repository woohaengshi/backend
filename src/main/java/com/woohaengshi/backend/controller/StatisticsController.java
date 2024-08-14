package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.controller.auth.MemberId;
import com.woohaengshi.backend.domain.statistics.StatisticsType;
import com.woohaengshi.backend.dto.response.statistics.ShowRankSnapshotResponse;
import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.ErrorResponse;
import com.woohaengshi.backend.exception.WoohaengshiException;
import com.woohaengshi.backend.service.statistics.StatisticsService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rank")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ShowRankSnapshotResponse getRanking(
            @PageableDefault(size = 100) Pageable pageable,
            @RequestParam(value = "type", defaultValue = "WEEKLY") StatisticsType statisticsType,
            @MemberId Long memberId) {

        return statisticsService.showRankData(memberId, statisticsType, pageable);
    }

}
