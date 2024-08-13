package com.woohaengshi.backend.service.studyrecord;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;

import java.time.YearMonth;

public interface StudyRecordService {
    public void save(SaveRecordRequest request, Long memberId);

    public ShowMonthlyRecordResponse showMonthlyRecord(YearMonth date, Long memberId);
}
