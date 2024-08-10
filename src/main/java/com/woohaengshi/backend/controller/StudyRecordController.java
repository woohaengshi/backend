package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.service.studyrecord.StudyRecordServiceImpl;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study-record")
public class StudyRecordController {

    private final StudyRecordServiceImpl studyRecordServiceImpl;

    @PostMapping
    public ResponseEntity<Void> saveStudyRecord(@Valid @RequestBody SaveRecordRequest request) {
        studyRecordServiceImpl.save(request, 1L);
        return ResponseEntity.created(URI.create("/api/v1/timers")).build();
    }
}
