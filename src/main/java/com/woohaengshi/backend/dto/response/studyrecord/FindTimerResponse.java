package com.woohaengshi.backend.dto.response.studyrecord;

import com.woohaengshi.backend.dto.response.subject.FindSubjectsResponse;

import lombok.Getter;

import java.util.List;

@Getter
public class FindTimerResponse {

    private int time;

    private List<FindSubjectsResponse> subjectsList;

    public FindTimerResponse(int time, List<FindSubjectsResponse> subjectsList) {
        this.time = time;
        this.subjectsList = subjectsList;
    }
}
