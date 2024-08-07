package com.woohaengshi.backend.domain;

import com.woohaengshi.backend.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monthly_time")
    private int monthlyTime;

    @Column(name = "weekly_time")
    private int weeklyTime;

    @Column(name = "daily_time")
    private int dailyTime;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Statistics() {
    }

    public Statistics(Long id, int monthlyTime, int weeklyTime, int dailyTime, Member member) {
        this.id = id;
        this.monthlyTime = monthlyTime;
        this.weeklyTime = weeklyTime;
        this.dailyTime = dailyTime;
        this.member = member;
    }
}
