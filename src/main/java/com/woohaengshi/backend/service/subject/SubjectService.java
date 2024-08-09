package com.woohaengshi.backend.service.subject;

import com.woohaengshi.backend.dto.response.studyrecord.ShowTimerResponse;

public interface SubjectService {
    ShowTimerResponse getTimer(Long memberId);
}
