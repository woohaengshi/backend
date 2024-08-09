package com.woohaengshi.backend.dto.response.studyrecord;

import lombok.Getter;

@Getter
public class FindCumulativeTimeResponse {

    private int time;

    public FindCumulativeTimeResponse(int time) {
        this.time = time;
    }
}
