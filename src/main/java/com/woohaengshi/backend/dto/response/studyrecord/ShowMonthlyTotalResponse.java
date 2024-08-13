package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.dto.result.MonthlyTotalRecordResult;

import lombok.Getter;

@Getter
public class ShowMonthlyTotalResponse {
    private int month;
    private int total;

    private ShowMonthlyTotalResponse(int month, int total) {
        this.month = month;
        this.total = total;
    }

    public static ShowMonthlyTotalResponse from(MonthlyTotalRecordResult result) {
        return new ShowMonthlyTotalResponse(result.getMonth(), result.getTotal());
    }
}
