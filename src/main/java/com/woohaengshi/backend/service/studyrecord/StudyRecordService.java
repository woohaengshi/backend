package com.woohaengshi.backend.service.studyrecord;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;

public interface StudyRecordService {
    public void save(SaveRecordRequest request, Long memberId);

    public ShowMonthlyRecordResponse showMonthlyRecord(int year, int month, Long memberId);
}
