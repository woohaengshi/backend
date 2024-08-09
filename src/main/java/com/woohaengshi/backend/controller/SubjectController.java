package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.response.studyrecord.FindTimerResponse;
import com.woohaengshi.backend.service.subject.SubjectService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subject")
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public FindTimerResponse showTimer() {
        return subjectService.getTimer(1L);
    }
}
