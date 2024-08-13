package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.dto.result.MonthlyTotalRecordResult;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowYearlyRecordResponse {
    private int year;
    private List<ShowMonthlyTotalResponse> records;

    private ShowYearlyRecordResponse(int year, List<ShowMonthlyTotalResponse> records) {
        this.year = year;
        this.records = records;
    }

    public static ShowYearlyRecordResponse of(int year, List<MonthlyTotalRecordResult> records) {
        return new ShowYearlyRecordResponse(
                year, records.stream().map(ShowMonthlyTotalResponse::from).toList());
    }
}
