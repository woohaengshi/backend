package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.dto.result.MonthlyTotalRecordResult;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowYearlyRecordResponse {
    private int year;
    private List<MonthlyTotalRecordResult> monthly;

    private ShowYearlyRecordResponse(int year, List<MonthlyTotalRecordResult> monthly) {
        this.year = year;
        this.monthly = monthly;
    }

    public static ShowYearlyRecordResponse of(int year, List<MonthlyTotalRecordResult> monthly) {
        return new ShowYearlyRecordResponse(year, monthly);
    }
}
