package com.woohaengshi.backend.dto.request.studyrecord;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class EditSubjectAndCommentRequest {
    private LocalDate date;
    private List<Long> addedSubject;
    private List<Long> deletedSubject;
    private String comment;

    public EditSubjectAndCommentRequest() {
    }

    public EditSubjectAndCommentRequest(LocalDate date, String comment) {
        this.date = date;
        this.comment = comment;
    }

    public EditSubjectAndCommentRequest(LocalDate date, List<Long> addedSubject, List<Long> deletedSubject, String comment) {
        this.date = date;
        this.addedSubject = addedSubject;
        this.deletedSubject = deletedSubject;
        this.comment = comment;
    }
}
