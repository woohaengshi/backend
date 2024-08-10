package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.service.studyrecord.StudyRecordService;
import com.woohaengshi.backend.service.studyrecord.StudyRecordServiceImpl;

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
}
