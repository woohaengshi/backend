package com.woohaengshi.backend.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimerControllerTest {
    @LocalServerPort private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void 현재_시간과_회원의_과목_목록을_조회_한다() {
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/subject")
                .then()
                .log()
                .all()
                .statusCode(200);
    }
}
