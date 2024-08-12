package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.domain.Subject;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowMonthlyRecordResponse {
    private int year;
    private int month;
    private List<ShowDailyRecordResponse> daily;

    private ShowMonthlyRecordResponse(int year, int month, List<ShowDailyRecordResponse> daily) {
        this.year = year;
        this.month = month;
        this.daily = daily;
    }

    public static ShowMonthlyRecordResponse of(int year, int month, List<Object[]> records) {
        return new ShowMonthlyRecordResponse(
                year,
                month,
                records.stream()
                        .map(
                                record ->
                                        ShowDailyRecordResponse.of(
                                                ((Number) record[0]).intValue(),
                                                ((Number) record[1]).intValue(),
                                                List.of(
                                                        Subject.builder()
                                                                .id((Long) record[2])
                                                                .name((String) record[3])
                                                                .build())))
                        .toList());
    }
}
