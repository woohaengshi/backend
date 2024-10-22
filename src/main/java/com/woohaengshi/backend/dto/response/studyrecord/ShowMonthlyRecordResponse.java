package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.domain.subject.Subject;
import com.woohaengshi.backend.dto.response.subject.ShowSubjectsResponse;
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
    private List<ShowSubjectsResponse> totalSubjects;

    private ShowMonthlyRecordResponse(
            int year,
            int month,
            List<ShowDailyRecordResponse> records,
            List<ShowSubjectsResponse> totalSubjects) {
        this.year = year;
        this.month = month;
        this.records = records;
        this.totalSubjects = totalSubjects;
    }

    public static ShowMonthlyRecordResponse of(
            YearMonth date, Map<Integer, ShowCalendarResult> calendar, List<Subject> subjects) {
        List<ShowDailyRecordResponse> records =
                calendar.keySet().stream()
                        .map(result -> ShowDailyRecordResponse.from(calendar.get(result)))
                        .toList();
        return new ShowMonthlyRecordResponse(
                date.getYear(),
                date.getMonthValue(),
                records,
                subjects.stream().map(ShowSubjectsResponse::from).toList());
    }
}
