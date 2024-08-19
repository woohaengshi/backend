package com.woohaengshi.backend.controller;

import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
    @LocalServerPort private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void 회원_정보를_조회한다() {
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/members")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }
}
