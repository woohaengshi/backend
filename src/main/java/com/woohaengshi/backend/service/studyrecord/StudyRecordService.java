package com.woohaengshi.backend.service.studyrecord;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.dto.response.studyrecord.ShowMonthlyRecordResponse;

import java.time.YearMonth;

public interface StudyRecordService {
    void save(SaveRecordRequest request, Long memberId);

    ShowMonthlyRecordResponse showMonthlyRecord(YearMonth date, Long memberId);

    void updateDailyStatistics();
    void updateWeeklyStatistics();
    void updateMonthlyStatistics();
    void updateTotalStatistics();
}
