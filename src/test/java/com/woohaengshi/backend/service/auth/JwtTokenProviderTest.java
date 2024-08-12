package com.woohaengshi.backend.service.auth;

import com.woohaengshi.backend.exception.WoohaengshiException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private static final String key =
            "55e32f10a70c4362a393055896bb17c955e32f10a70c4362a393055896bb17c9";
    private static final Long accessExpiration = 100000L;

    @Test
    void 유효기간이_지난_토큰은_예외를_던진다() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(key, 0L);
        String accessToken = jwtTokenProvider.createAccessToken(1L);
        assertThatThrownBy(() -> jwtTokenProvider.validToken(accessToken))
                .isExactlyInstanceOf(WoohaengshiException.class);
    }

    @Test
    void 토큰_생성() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(key, accessExpiration);
        String accessToken = jwtTokenProvider.createAccessToken(1L);
        assertThat(accessToken).isNotBlank();
    }

    @Test
    void 토큰의_claim을_가져올_수_있다() {
        Long MEMBER_ID = 1L;
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(key, accessExpiration);
        String accessToken = jwtTokenProvider.createAccessToken(MEMBER_ID);
        assertThat(jwtTokenProvider.getMemberId(accessToken)).isEqualTo(MEMBER_ID);
    }
}
