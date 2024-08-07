package com.woohaengshi.backend.domain.member;

public enum Course {
    CLOUD_SERVICE("클라우드 서비스"),
    CLOUD_ENGINEERING("클라우드 엔지니어링"),
    AI_ENGINEERING("AI 엔지니어링");

    private final String name;

    Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
