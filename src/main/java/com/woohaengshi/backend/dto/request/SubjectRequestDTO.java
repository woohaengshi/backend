package com.woohaengshi.backend.dto.request;

import java.util.List;

public class SubjectRequestDTO {
    private List<String> addSubjects;
    private List<Long> deleteSubjects;

    public List<String> getAddSubjects() {
        return addSubjects;
    }

    public List<Long> getDeleteSubjects () {
        return deleteSubjects;
    }
}
