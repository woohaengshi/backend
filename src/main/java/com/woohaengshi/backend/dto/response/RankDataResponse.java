package com.woohaengshi.backend.dto.response;

import com.woohaengshi.backend.domain.member.Member;

import lombok.Getter;

@Getter
public class RankDataResponse {
    private String course;
    private String name;
    private String image;
    private Integer rank;
    private Integer studyTime;
    private Integer totalTime;

    private RankDataResponse(
            String course,
            String name,
            String image,
            Integer rank,
            Integer studyTime,
            Integer totalTime) {
        this.course = course;
        this.name = name;
        this.image = image;
        this.rank = rank;
        this.studyTime = studyTime;
        this.totalTime = totalTime;
    }

    public static RankDataResponse of(Member member, int rank, int time, int totalTime) {
        return new RankDataResponse(
                member.getCourse().getName(),
                member.getName(),
                member.getImage(),
                rank,
                time,
                totalTime);
    }
}
