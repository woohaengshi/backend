package com.woohaengshi.backend.dto.response.subject;

import lombok.Getter;

@Getter
public class ShowSubjectsResponse {

    private Long id;

    private String name;

    public ShowSubjectsResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
