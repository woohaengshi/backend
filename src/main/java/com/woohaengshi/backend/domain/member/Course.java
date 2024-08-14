package com.woohaengshi.backend.domain.member;

import static com.woohaengshi.backend.exception.ErrorCode.COURSE_NOT_FOUND;

import com.woohaengshi.backend.exception.WoohaengshiException;

import java.util.Arrays;

public enum Course {
    CLOUD_SERVICE("클라우드 서비스"),
    CLOUD_ENGINEERING("클라우드 엔지니어링"),
    AI_ENGINEERING("AI 엔지니어링");

    private final String name;

    Course(String name) {
        this.name = name;
    }

    public static Course from(String name) {
        return Arrays.stream(Course.values())
                .filter(course -> course.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new WoohaengshiException(COURSE_NOT_FOUND));
    }

    public String getName() {
        return name;
    }
}
