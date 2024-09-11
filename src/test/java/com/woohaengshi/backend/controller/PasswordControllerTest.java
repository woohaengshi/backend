package com.woohaengshi.backend.controller;

import static org.springframework.http.HttpStatus.OK;

import com.woohaengshi.backend.dto.request.password.ChangePasswordRequest;
import com.woohaengshi.backend.dto.request.password.SendMailRequest;
import com.woohaengshi.backend.support.ControllerTest;

import org.junit.jupiter.api.Test;

class PasswordControllerTest extends ControllerTest {

    private String authenticateCode = "a1f3ac33-bfa5-4c0d-aede-aed68609558d";

    @Test
    void 비밀번호_재설정_메일을_보낼_수_있다() {
        SendMailRequest request = new SendMailRequest("rlfrkdms1@naver.com", "클라우드 서비스", "길가은");
        baseRestAssured()
                .body(request)
                .when()
                .post("/api/v1/password")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }

    @Test
    void 인증코드를_검증_할_수_있다() {
        baseRestAssured()
                .when()
                .get("/api/v1/password/" + authenticateCode)
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }

    @Test
    void 비밀번호를_재설정_할_수_있다() {
        ChangePasswordRequest request = new ChangePasswordRequest("newPassword11!!");
        baseRestAssured()
                .body(request)
                .when()
                .post("/api/v1/password/" + authenticateCode)
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }
}
