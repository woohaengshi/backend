package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.domain.Subject;
import com.woohaengshi.backend.dto.response.subject.ShowSubjectsResponse;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowDailyRecordResponse {
    private int day;
    private int time;
    private List<ShowSubjectsResponse> subjects;

    private ShowDailyRecordResponse(int day, int time, List<ShowSubjectsResponse> subjects) {
        this.day = day;
        this.time = time;
        this.subjects = subjects;
    }

    public static ShowDailyRecordResponse of(
            int day, int time, List<ShowSubjectsResponse> subjects) {
        return new ShowDailyRecordResponse(day, time, subjects);
    }
}
