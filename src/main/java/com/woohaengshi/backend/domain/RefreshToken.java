package com.woohaengshi.backend.domain;

import com.woohaengshi.backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

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
    private Long expirationTime;

    protected RefreshToken() {
    }

    @Builder
    private RefreshToken(Long expirationTime, Long id, Member member, String token) {
        this.expirationTime = expirationTime;
        this.id = id;
        this.member = member;
        this.token = token;
    }
}
