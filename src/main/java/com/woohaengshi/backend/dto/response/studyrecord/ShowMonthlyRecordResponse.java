package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.dto.result.ShowCalendarResult;

import lombok.Getter;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Getter
public class ShowMonthlyRecordResponse {
    private int year;
    private int month;
    private List<ShowDailyRecordResponse> records;

    private ShowMonthlyRecordResponse(int year, int month, List<ShowDailyRecordResponse> records) {
        this.year = year;
        this.month = month;
        this.records = records;
    }

    public static ShowMonthlyRecordResponse of(
            YearMonth date, Map<Integer, ShowCalendarResult> calendar) {
        List<ShowDailyRecordResponse> records =
                calendar.keySet().stream()
                        .map(result -> ShowDailyRecordResponse.from(calendar.get(result)))
                        .toList();
        return new ShowMonthlyRecordResponse(date.getYear(), date.getMonthValue(), records);
    }
}
