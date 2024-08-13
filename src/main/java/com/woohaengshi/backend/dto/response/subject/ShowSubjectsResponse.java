package com.woohaengshi.backend.dto.response.subject;

import com.woohaengshi.backend.domain.subject.Subject;

import lombok.Getter;

@Getter
public class ShowSubjectsResponse {

    private Long id;

    private String name;

    private ShowSubjectsResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ShowSubjectsResponse from(Subject subject) {
        return new ShowSubjectsResponse(subject.getId(), subject.getName());
    }
}
