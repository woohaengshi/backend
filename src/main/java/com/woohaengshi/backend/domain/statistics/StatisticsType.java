package com.woohaengshi.backend.domain.statistics;

import com.woohaengshi.backend.exception.ErrorCode;
import com.woohaengshi.backend.exception.WoohaengshiException;

public enum StatisticsType {
    MONTHLY,
    WEEKLY,
    DAILY;

    public static StatisticsType fromString(String type) {
        switch (type.toUpperCase()) {
            case "MONTHLY":
                return MONTHLY;
            case "WEEKLY":
                return WEEKLY;
            case "DAILY":
                return DAILY;
            default:
                throw new WoohaengshiException(ErrorCode.STATISTICS_TYPE_NOT_FOUND);
        }
    }
}
