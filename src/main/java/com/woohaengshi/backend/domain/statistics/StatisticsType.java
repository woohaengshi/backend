package com.woohaengshi.backend.domain.statistics;

public enum StatisticsType {
    DAILY("dailyTime"),
    WEEKLY("weeklyTime"),
    MONTHLY("monthlyTime");

    private final String fieldName;

    StatisticsType(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
