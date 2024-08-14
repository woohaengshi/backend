package com.woohaengshi.backend.controller;

import static com.woohaengshi.backend.exception.ErrorCode.INVALID_INPUT;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.woohaengshi.backend.dto.request.studyrecord.SaveRecordRequest;
import com.woohaengshi.backend.support.ControllerTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

class StudyRecordControllerTest extends ControllerTest {

    @Test
    void 공부를_기록할_수_있다() {
        SaveRecordRequest request = new SaveRecordRequest(LocalDate.now(), 10, List.of(1L, 2L));
        baseRestAssuredWithAuth()
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
        baseRestAssuredWithAuth()
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
        baseRestAssuredWithAuth()
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
        baseRestAssuredWithAuth()
                .queryParam("date", YearMonth.now().toString())
                .when()
                .get("/api/v1/study-record/monthly")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }

    @Test
    void 날짜가_전달되지_않으면_현재를_기준으로_공부_기록을_조회할_수_있다() {
        baseRestAssuredWithAuth()
                .when()
                .get("/api/v1/study-record/monthly")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }

    @Test
    void 연도를_통해_공부_기록을_조회할_수_있다() {
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .queryParam("year", 2024)
                .when()
                .get("/api/v1/study-record/yearly")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }

    @Test
    void 연도가_전달되지_않으면_공부_기록을_조회할_수_없다() {
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/study-record/yearly")
                .then()
                .log()
                .all()
                .statusCode(BAD_REQUEST.value());
    }
}
