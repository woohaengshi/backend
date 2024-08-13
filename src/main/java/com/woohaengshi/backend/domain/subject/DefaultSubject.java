package com.woohaengshi.backend.domain.subject;

import static com.woohaengshi.backend.domain.member.Course.*;

import com.woohaengshi.backend.domain.member.Course;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum DefaultSubject {
    JAVA(CLOUD_SERVICE, "Java"),
    JAVA_SCRIPT(CLOUD_SERVICE, "Java Script"),
    DATABASE(CLOUD_SERVICE, "Database"),
    LINUX(CLOUD_ENGINEERING, "Linux"),
    DEVOPS(CLOUD_ENGINEERING, "DevOps"),
    DOCKER(CLOUD_ENGINEERING, "Docker"),
    PYTHON(AI_ENGINEERING, "Python"),
    OPEN_AI(AI_ENGINEERING, "Open API"),
    STREAM_LIT(AI_ENGINEERING, "Streamlit");

    private final Course course;
    private final String value;

    DefaultSubject(Course course, String value) {
        this.course = course;
        this.value = value;
    }

    public static List<String> getDefaultSubjects(Course course) {
        return Arrays.stream(DefaultSubject.values())
                .filter(subject -> subject.course == course)
                .map(DefaultSubject::getValue)
                .toList();
    }
}
