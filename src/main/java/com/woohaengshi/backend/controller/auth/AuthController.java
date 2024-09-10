package com.woohaengshi.backend.controller.auth;

import static com.woohaengshi.backend.controller.auth.CookieProvider.REFRESH_TOKEN;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.woohaengshi.backend.dto.request.auth.SignInRequest;
import com.woohaengshi.backend.dto.request.auth.SignUpRequest;
import com.woohaengshi.backend.dto.response.auth.SignInResponse;
import com.woohaengshi.backend.dto.result.SignInResult;
import com.woohaengshi.backend.service.auth.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final CookieProvider cookieProvider;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
            @RequestPart(value="signUpRequest") SignUpRequest signUpRequest,
            @RequestPart(value="image", required = false) MultipartFile image
    ) {
        authService.signUp(signUpRequest, image);
        return ResponseEntity.created(URI.create("/api/v1/sign-in")).build();
    }


    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        SignInResult result = authService.signIn(signInRequest);
        return ResponseEntity.ok()
                .header(
                        SET_COOKIE,
                        cookieProvider
                                .createRefreshTokenCookie(result.getRefreshToken())
                                .toString())
                .body(result.getSignInResponse());
    }

    @PostMapping("/reissue")
    public ResponseEntity<SignInResponse> reissue(
            @CookieValue(name = REFRESH_TOKEN, required = false) String refreshToken) {
        SignInResult result = authService.reissue(refreshToken);
        return ResponseEntity.ok()
                .header(
                        SET_COOKIE,
                        cookieProvider
                                .createRefreshTokenCookie(result.getRefreshToken())
                                .toString())
                .body(result.getSignInResponse());
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            @CookieValue(name = REFRESH_TOKEN, required = false) String refreshToken) {
        authService.signOut(refreshToken);
        return ResponseEntity.noContent()
                .header(SET_COOKIE, cookieProvider.createSignOutCookie().toString())
                .build();
    }
}
