package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.SaveRecordRequest;
import com.woohaengshi.backend.service.StudyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StudyRecordController {

    private final StudyRecordService studyRecordService;

    @PostMapping("/timers")
    public ResponseEntity<Void> saveStudyRecord(
            @RequestBody SaveRecordRequest request, Long memberId) {
        studyRecordService.save(request, memberId);
        return ResponseEntity.created(URI.create("/api/v1/timers")).build();
    }
}
