package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.dto.response.subject.ShowSubjectsResponse;

import com.woohaengshi.backend.dto.result.DailyStudyRecordResult;
import lombok.Getter;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
            YearMonth date, List<DailyStudyRecordResult> results) {
        Map<Integer, List<ShowSubjectsResponse>> subjectsMap = new HashMap<>();
        Map<Integer, Integer> timeMap = new HashMap<>();

        for (DailyStudyRecordResult result : results) {
            subjectsMap
                    .computeIfAbsent(result.getDay(), k -> new ArrayList<>())
                    .add(ShowSubjectsResponse.of(result.getSubjectId(), result.getSubjectName()));
            timeMap.putIfAbsent(result.getDay(), result.getTime());
        }

        List<ShowDailyRecordResponse> records = new ArrayList<>();
        for (int day = 1; day <= date.lengthOfMonth(); day++) {
            List<ShowSubjectsResponse> subjects = subjectsMap.getOrDefault(day, new ArrayList<>());
            int time = timeMap.getOrDefault(day, 0);
            records.add(ShowDailyRecordResponse.of(day, time, subjects));
        }

        return new ShowMonthlyRecordResponse(date.getYear(), date.getMonthValue(), records);
    }
}
