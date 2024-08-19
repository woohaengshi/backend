package com.woohaengshi.backend.dto.result;

import lombok.Getter;

@Getter
public class DailyStudyRecordResult {
    private int day;
    private int time;
    private Long subjectId;
    private String subjectName;

    public DailyStudyRecordResult(int day, int time, Long subjectId, String subjectName) {
        this.day = day;
        this.time = time;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }
}
