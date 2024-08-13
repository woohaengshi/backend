package com.woohaengshi.backend.controller.auth;

import static com.woohaengshi.backend.controller.auth.RefreshCookieProvider.REFRESH_TOKEN;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.woohaengshi.backend.dto.request.auth.SignInRequest;
import com.woohaengshi.backend.dto.request.auth.SignUpRequest;
import com.woohaengshi.backend.dto.response.auth.SignInResponse;
import com.woohaengshi.backend.dto.result.SignInResult;
import com.woohaengshi.backend.service.auth.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.created(URI.create("/api/v1/sign-in")).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        SignInResult result = authService.signIn(signInRequest);
        return ResponseEntity.ok()
                .header(SET_COOKIE, result.getRefreshTokenCookie().toString())
                .body(result.getSignInResponse());
    }

    @PostMapping("/reissue")
    public ResponseEntity<SignInResponse> reissue(
            @CookieValue(name = REFRESH_TOKEN, required = false) String refreshToken) {
        SignInResult result = authService.reissue(refreshToken);
        return ResponseEntity.ok()
                .header(SET_COOKIE, result.getRefreshTokenCookie().toString())
                .body(result.getSignInResponse());
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            @CookieValue(name = REFRESH_TOKEN, required = false) String refreshToken) {
        ResponseCookie signOutCookie = authService.signOut(refreshToken);
        return ResponseEntity.ok().header(SET_COOKIE, signOutCookie.toString()).build();
    }
}
