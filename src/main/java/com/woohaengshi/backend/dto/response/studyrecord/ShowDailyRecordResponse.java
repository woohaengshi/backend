package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.dto.response.subject.ShowSubjectsResponse;
import com.woohaengshi.backend.dto.result.ShowCalendarResult;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowDailyRecordResponse {
    private int day;
    private int time;
    private String comment;
    private List<ShowSubjectsResponse> subjects;

    private ShowDailyRecordResponse(
            int day, int time, String comment, List<ShowSubjectsResponse> subjects) {
        this.day = day;
        this.time = time;
        this.comment = comment;
        this.subjects = subjects;
    }

    public static ShowDailyRecordResponse from(ShowCalendarResult result) {
        return new ShowDailyRecordResponse(
                result.getDay(),
                result.getTime(),
                result.getComment(),
                result.getSubjects().stream().map(ShowSubjectsResponse::from).toList());
    }
}
