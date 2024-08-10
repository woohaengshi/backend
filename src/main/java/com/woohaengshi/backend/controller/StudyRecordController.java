package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;
import com.woohaengshi.backend.service.studyrecord.StudyRecordService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study-record")
public class StudyRecordController {

    private final StudyRecordService studyRecordService;

    @PostMapping
    public ResponseEntity<Void> saveStudyRecord(@Valid @RequestBody SaveRecordRequest request) {
        studyRecordService.save(request, 1L);
        return ResponseEntity.created(URI.create("/api/v1/timers")).build();
    }

    @GetMapping("/monthly")
    public ShowMonthlyRecordResponse getMonthlyRecords(
            @RequestParam("year") int year, @RequestParam("month") int month) {
        return studyRecordService.showMonthlyRecord(year, month, 1L);
    }
}
