package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;
import com.woohaengshi.backend.dto.response.studyrecord.ShowYearlyRecordResponse;
import com.woohaengshi.backend.service.studyrecord.StudyRecordService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study-record")
public class StudyRecordController {

    private final StudyRecordService studyRecordService;

    @PostMapping
    public ResponseEntity<Void> saveStudyRecord(@Valid @RequestBody SaveRecordRequest request) {
        studyRecordService.save(request, 1L);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/monthly")
    public ShowMonthlyRecordResponse getMonthlyRecords(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM") YearMonth date) {
        return studyRecordService.showMonthlyRecord(date, 16L);
    }

    @GetMapping("/yearly")
    public ShowYearlyRecordResponse getYearlyRecords(@RequestParam("year") int year) {
        return studyRecordService.showYearlyRecord(year, 16L);
    }
}
