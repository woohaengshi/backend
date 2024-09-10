package com.woohaengshi.backend.repository;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;

@Getter
@RedisHash("authentication_code")
public class AuthenticationCode {

    @Id
    private String code;
    private Long memberId;

    @TimeToLive
    private Long expiredAt;

    protected AuthenticationCode() {
    }

    public AuthenticationCode(Long memberId) {
        this.code = UUID.randomUUID().toString();
        this.memberId = memberId;
        this.expiredAt = 5000L;
    }
}
