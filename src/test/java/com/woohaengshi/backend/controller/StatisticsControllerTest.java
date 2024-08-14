package com.woohaengshi.backend.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatisticsControllerTest {

    @LocalServerPort private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void getRanking_정상_요청() {
        RestAssured.given()
                .log()
                .all()
                .param("page", 0)
                .param("type", "DAILY")
                .param("size", 10)
                .contentType(ContentType.JSON)
                .get("/api/v1/rank")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Test
    void 페이지가_잘_못_온_경우() {
        RestAssured.given()
                .log()
                .all()
                .param("page", -1)
                .param("type", "DAILY")
                .param("size", 10)
                .contentType(ContentType.JSON)
                .get("/api/v1/rank")
                .then()
                .log()
                .all()
                .statusCode(400);
    }

    @Test
    void 타입이_잘_못_온_경우() {
        RestAssured.given()
                .log()
                .all()
                .param("page", -1)
                .param("type", "asd")
                .param("size", 10)
                .contentType(ContentType.JSON)
                .get("/api/v1/rank")
                .then()
                .log()
                .all()
                .statusCode(400);
    }

    @Test
    void 사이즈가_너무_작은_경우() {
        RestAssured.given()
                .log()
                .all()
                .param("page", 0)
                .param("type", "DAILY")
                .param("size", 0)
                .contentType(ContentType.JSON)
                .get("/api/v1/rank")
                .then()
                .log()
                .all()
                .statusCode(400);
    }
}
