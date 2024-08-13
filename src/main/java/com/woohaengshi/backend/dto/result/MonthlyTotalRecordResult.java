package com.woohaengshi.backend.dto.result;

import lombok.Getter;

@Getter
public class MonthlyTotalRecordResult {
    private int month;
    private int total;

    public MonthlyTotalRecordResult(int month, Long total) {
        this.month = month;
        this.total = total.intValue();
    }
}
