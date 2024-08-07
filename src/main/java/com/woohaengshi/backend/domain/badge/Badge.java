package com.woohaengshi.backend.domain.badge;

import com.woohaengshi.backend.domain.member.Member;

import jakarta.persistence.*;

import lombok.Getter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Name name;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected Badge() {}

    public Badge(Long id, Name name, Member member, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.member = member;
        this.createdAt = createdAt;
    }
}
