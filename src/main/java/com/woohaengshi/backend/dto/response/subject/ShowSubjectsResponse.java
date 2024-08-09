package com.woohaengshi.backend.dto.response.subject;

import lombok.Getter;

@Getter
public class ShowSubjectsResponse {

    private Long id;

    private String name;

    private ShowSubjectsResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ShowSubjectsResponse of(Long id, String name) {
        return new ShowSubjectsResponse(id, name);
    }
}
