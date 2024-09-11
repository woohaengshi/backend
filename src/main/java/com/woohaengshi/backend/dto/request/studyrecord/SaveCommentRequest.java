package com.woohaengshi.backend.dto.request.studyrecord;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SaveCommentRequest {
    private LocalDate date;
    private String comment;

    private SaveCommentRequest() {}

    public SaveCommentRequest(LocalDate date, String comment) {
        this.date = date;
        this.comment = comment;
    }
}
