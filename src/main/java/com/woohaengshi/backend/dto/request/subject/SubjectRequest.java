package com.woohaengshi.backend.dto.request.subject;

import java.util.List;

public class SubjectRequest {
    private List<String> addSubjects;
    private List<Long> deleteSubjects;

    public List<String> getAddSubjects() {
        return addSubjects;
    }

    public List<Long> getDeleteSubjects () {
        return deleteSubjects;
    }
}
