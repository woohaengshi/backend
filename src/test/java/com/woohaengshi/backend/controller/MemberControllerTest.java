package com.woohaengshi.backend.controller;

import static org.springframework.http.HttpStatus.OK;

import com.woohaengshi.backend.support.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

public class MemberControllerTest extends ControllerTest {
    @Test
    void 회원_정보를_조회한다() {
        baseRestAssuredWithAuth()
                .when()
                .get("/api/v1/members")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }
}
