package com.woohaengshi.backend.dto.request.studyrecord;

import com.woohaengshi.backend.domain.StudyRecord;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SaveCommentRequest {
    @PastOrPresent private LocalDate date;
    private String comment;

    private SaveCommentRequest() {}

    public SaveCommentRequest(LocalDate date, String comment) {
        this.date = date;
        this.comment = comment;
    }
}
