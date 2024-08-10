package com.woohaengshi.backend.support.fixture;

import com.woohaengshi.backend.domain.member.Member;
import com.woohaengshi.backend.domain.statistics.Statistics;

public class StatisticsFixture {

    private Long id;
    private int monthlyTime = 100;
    private int weeklyTime = 12;
    private int dailyTime = 4;
    private int totalTime = 104;
    private Member member;

    public static StatisticsFixture builder() {
        return new StatisticsFixture();
    }

    public StatisticsFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StatisticsFixture dailyTime(int time) {
        this.dailyTime = time;
        return this;
    }

    public StatisticsFixture weeklyTime(int time) {
        this.weeklyTime = time;
        return this;
    }

    public StatisticsFixture monthlyTime(int time) {
        this.monthlyTime = time;
        return this;
    }

    public StatisticsFixture totalTime(int time) {
        this.totalTime = time;
        return this;
    }

    public StatisticsFixture member(Member member) {
        this.member = member;
        return this;
    }

    public Statistics build() {
        return Statistics.builder()
                .id(id)
                .dailyTime(dailyTime)
                .weeklyTime(weeklyTime)
                .monthlyTime(monthlyTime)
                .totalTime(totalTime)
                .member(member)
                .build();
    }
}
