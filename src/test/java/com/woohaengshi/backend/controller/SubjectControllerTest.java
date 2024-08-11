package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.subject.SubjectRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static com.woohaengshi.backend.exception.ErrorCode.SUBJECT_ALREADY_EXISTS;
import static com.woohaengshi.backend.exception.ErrorCode.SUBJECT_NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubjectControllerTest {
    @LocalServerPort private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void 과목을_편집할_수_있다() {
        SubjectRequest request = new SubjectRequest(List.of("Java", "Spring"), List.of(4L, 5L));

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/subjects")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }

    @Test
    void 존재하지_않는_과목을_삭제할_수_없다() {
        SubjectRequest request = new SubjectRequest(List.of(), List.of(100L));

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/subjects")
                .then()
                .log()
                .all()
                .statusCode(SUBJECT_NOT_FOUND.getStatus().value());
    }

    @Test
    void 이미_존재하는_과목을_추가할_수_없다() {
        SubjectRequest request = new SubjectRequest(List.of("Spring"), List.of());

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/subjects")
                .then()
                .log()
                .all()
                .statusCode(SUBJECT_ALREADY_EXISTS.getStatus().value());
    }
}
