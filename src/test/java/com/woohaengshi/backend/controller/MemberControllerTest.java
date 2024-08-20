package com.woohaengshi.backend.controller;

import static org.springframework.http.HttpStatus.OK;

import com.woohaengshi.backend.support.ControllerTest;

import org.junit.jupiter.api.Test;

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
