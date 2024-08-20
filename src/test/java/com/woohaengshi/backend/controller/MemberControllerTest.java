package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.member.ChangePasswordRequest;
import com.woohaengshi.backend.support.ControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpStatus.OK;

class MemberControllerTest extends ControllerTest {

    @Test
    void 비밀번호를_변경_할_수_있다(){
        ChangePasswordRequest request = new ChangePasswordRequest("password12!@", "newPassword12!@");
        baseRestAssuredWithAuth()
                .body(request)
                .when()
                .post("/api/v1/members")
                .then()
                .log()
                .all()
                .statusCode(OK.value());
    }

}
