package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.subject.SubjectRequest;
import com.woohaengshi.backend.service.subject.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;
    @PostMapping
    public ResponseEntity<Void> edit(@RequestBody SubjectRequest requestDTO) {
        Long memberId = 1L;
        subjectService.editSubjects(memberId, requestDTO);
        return ResponseEntity.ok().build();
    }
}
