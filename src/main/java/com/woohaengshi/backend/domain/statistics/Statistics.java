package com.woohaengshi.backend.domain.statistics;

import com.woohaengshi.backend.domain.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Builder;
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

    @Column(name = "total_time")
    private int totalTime;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    protected Statistics() {}

    @Builder
    public Statistics(Long id, int monthlyTime, int weeklyTime, int totalTime, Member member) {
        this.id = id;
        this.monthlyTime = monthlyTime;
        this.weeklyTime = weeklyTime;
        this.member = member;
        this.totalTime = totalTime;
    }

    public Statistics(Member member) {
        this.member = member;
    }

    public void changeTime(StatisticsType statisticsType, int time) {
        if (statisticsType == StatisticsType.WEEKLY) {
            this.weeklyTime = time;
            return;
        }
        if (statisticsType == StatisticsType.MONTHLY) {
            this.monthlyTime = time;
            return;
        }

        this.totalTime = time;
    }
}
