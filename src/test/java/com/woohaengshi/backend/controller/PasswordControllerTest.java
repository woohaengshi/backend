package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.password.SendMailRequest;
import com.woohaengshi.backend.support.ControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpStatus.OK;

class PasswordControllerTest extends ControllerTest {

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

}