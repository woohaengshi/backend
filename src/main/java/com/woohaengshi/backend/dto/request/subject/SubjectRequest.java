package com.woohaengshi.backend.dto.request.subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SubjectRequest {
    private List<String> addSubjects;
    private List<Long> deleteSubjects;

    public void setSubjectsForAddition(List<String> subjects) {
        this.addSubjects = subjects;
    }

    public void setSubjectsForDeletion(List<Long> subjectIds) {
        this.deleteSubjects = subjectIds;
    }
}
