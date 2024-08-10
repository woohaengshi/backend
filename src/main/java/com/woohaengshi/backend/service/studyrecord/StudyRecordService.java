package com.woohaengshi.backend.service.studyrecord;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;

public interface StudyRecordService {
    public void save(SaveRecordRequest request, Long memberId);
}
