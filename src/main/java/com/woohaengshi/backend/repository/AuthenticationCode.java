package com.woohaengshi.backend.repository;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;

@Getter
@RedisHash("AuthenticationCode")
public class AuthenticationCode {

    @Id private String code;
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
