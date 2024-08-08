package com.woohaengshi.backend.controller;

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
    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void 공부를_기록할_수_있다(){
        SaveRecordRequest request =
                new SaveRecordRequest(LocalDate.now(), 10, List.of("HTML", "CSS"));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/v1/timers")
                .then().log().all().statusCode(201);
    }

}
