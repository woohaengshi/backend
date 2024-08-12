package com.woohaengshi.backend.service;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;

public interface StudyRecordService {
    void save(SaveRecordRequest request, Long memberId);
}
