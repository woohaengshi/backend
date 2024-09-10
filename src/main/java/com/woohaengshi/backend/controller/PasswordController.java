package com.woohaengshi.backend.controller;

import com.woohaengshi.backend.dto.request.password.ChangePasswordRequest;
import com.woohaengshi.backend.dto.request.password.SendMailRequest;
import com.woohaengshi.backend.service.password.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody @Valid SendMailRequest request) {
        passwordService.sendMail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{authenticationCode}")
    public ResponseEntity<Void> changePassword(@PathVariable String authenticationCode, @RequestBody @Valid ChangePasswordRequest request) {
        passwordService.changePassword(authenticationCode, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{authenticationCode}")
    public ResponseEntity<Void> validateCode(@PathVariable String authenticationCode) {
        passwordService.validateCode(authenticationCode);
        return ResponseEntity.ok().build();
    }
}
