package com.woohaengshi.backend.controller;

import static com.woohaengshi.backend.exception.ErrorCode.INVALID_INPUT;

import static org.springframework.http.HttpStatus.OK;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudyRecordControllerTest {
    @LocalServerPort private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void 공부를_기록할_수_있다() {
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/study-record")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }

    @Test
    void 시간이_0일경우_공부를_기록할_수_없다() {
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 0, List.of(1L, 2L));
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/study-record")
                .then()
                .log()
                .all()
                .statusCode(INVALID_INPUT.getStatus().value());
    }

    @Test
    void 날짜가_미래일_경우_공부를_기록할_수_없다() {
        SaveRecordRequest request =
                new SaveRecordRequest(LocalDate.now().plusDays(2), 0, List.of(1L, 2L));
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/study-record")
                .then()
                .log()
                .all()
                .statusCode(INVALID_INPUT.getStatus().value());
    }

    @Test
    void 연도와_월을_통해_공부_기록을_조회할_수_있다() {
        int year = 2024;
        int month = 8;

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .queryParam("year", year)
                .queryParam("month", month)
                .when()
                .get("/api/v1/study-record/monthly")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Test
    void 월이_전달되지_않으면_공부_기록을_조회할_수_없다() {
        int year = 2024;

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .queryParam("year", year)
                .when()
                .get("/api/v1/study-record/monthly")
                .then()
                .log()
                .all()
                .statusCode(400);
    }

    @Test
    void 연도가_전달되지_않으면_공부_기록을_조회할_수_없다() {
        int month = 8;

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .queryParam("month", month)
                .when()
                .get("/api/v1/study-record/monthly")
                .then()
                .log()
                .all()
                .statusCode(400);
    }
}
