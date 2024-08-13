package com.woohaengshi.backend.domain;

import com.woohaengshi.backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    protected RefreshToken() {
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expirationTime);
    }

    @Builder
    public RefreshToken(Long expirationSeconds, Long id, Member member) {
        this.expirationTime = LocalDateTime.now().plusSeconds(expirationSeconds);
        this.id = id;
        this.member = member;
        this.token = createToken();
    }

    public void reissue(Long expirationSeconds) {
        this.token = createToken();
        this.expirationTime = LocalDateTime.now().plusSeconds(expirationSeconds);
    }

    private String createToken(){
        return UUID.randomUUID().toString();
}

}
