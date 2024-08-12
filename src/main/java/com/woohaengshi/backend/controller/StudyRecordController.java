package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.controller.auth.MemberId;
import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.service.StudyRecordService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study-record")
public class StudyRecordController {

    private final StudyRecordService studyRecordService;

    @PostMapping
    public ResponseEntity<Void> saveStudyRecord(@Valid @RequestBody SaveRecordRequest request, @MemberId Long memberId) {
        studyRecordService.save(request, memberId);
        return ResponseEntity.ok().build();
    }
}
