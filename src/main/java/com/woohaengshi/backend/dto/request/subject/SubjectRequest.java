package com.woohaengshi.backend.dto.request.subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {
    private List<String> addSubjects;
    private List<Long> deleteSubjects;

    public List<String> getAddSubjects() {
        return addSubjects;
    }

    public List<Long> getDeleteSubjects () {
        return deleteSubjects;
    }

    public void setSubjectsForAddition(List<String> subjects) {
        this.addSubjects = subjects;
    }

    public void setSubjectsForDeletion(List<Long> subjectIds) {
        this.deleteSubjects = subjectIds;
    }
}
