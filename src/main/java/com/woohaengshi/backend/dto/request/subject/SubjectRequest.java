package com.woohaengshi.backend.dto.request.subject;

import lombok.Getter;

import java.util.List;

@Getter
public class SubjectRequest {
    private List<String> addedSubjects;
    private List<Long> deletedSubjects;

    public SubjectRequest(List<String> addedSubjects, List<Long> deletedSubjects) {
        this.addedSubjects = addedSubjects;
        this.deletedSubjects = deletedSubjects;
    }
}
