package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.SendMailRequest;
import com.woohaengshi.backend.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService mailService;

    @PostMapping("/api/v1/mail")
    public void send(@RequestBody @Valid SendMailRequest request) {
        mailService.sendMail(request);
    }
}
