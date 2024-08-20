package com.woohaengshi.backend.service.studyrecord;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;
import com.woohaengshi.backend.dto.response.studyrecord.ShowYearlyRecordResponse;

import java.time.YearMonth;

public interface StudyRecordService {
    void save(SaveRecordRequest request, Long memberId);

    ShowYearlyRecordResponse showYearlyRecord(int year, Long memberId);

    ShowMonthlyRecordResponse getMonthlyRecord(YearMonth date, Long memberId);
}
