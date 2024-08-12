package com.woohaengshi.backend.dto.request.subject;

import lombok.Getter;

import java.util.List;

@Getter
public class SubjectRequest {
    private List<String> addSubjects;
    private List<Long> deleteSubjects;

    public SubjectRequest(List<String> addSubjects, List<Long> deleteSubjects) {
        this.addSubjects = addSubjects;
        this.deleteSubjects = deleteSubjects;
    }
}
