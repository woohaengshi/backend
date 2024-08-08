package com.woohaengshi.backend.dto.response.subject;

import lombok.Getter;

@Getter
public class FindSubjectsResponse {

    private Long id;

    private String name;

    public FindSubjectsResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
