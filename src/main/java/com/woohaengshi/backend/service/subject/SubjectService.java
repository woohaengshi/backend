package com.woohaengshi.backend.service.subject;

import com.woohaengshi.backend.dto.response.studyrecord.FindTimerResponse;

import java.time.LocalDate;

public interface SubjectService {
    FindTimerResponse findTimer(Long memberId, LocalDate date);
}
