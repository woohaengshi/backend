package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.SubjectRequestDTO;
import com.woohaengshi.backend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SubjectController {

  @Autowired
  private SubjectService subjectService;
  @PostMapping("/api/v1/subjects")
  public ResponseEntity<Void> edit(@RequestBody SubjectRequestDTO requestDTO) {
    Long memberId = 1L;
    subjectService.editSubjects(memberId, requestDTO);
    return ResponseEntity.created(URI.create("/api/v1/subjects")).build();
  }
}
