package com.woohaengshi.backend.dto.response.statistics;

import com.woohaengshi.backend.domain.member.Member;

import lombok.Getter;

@Getter
public class RankDataResponse {
    private Long id;
    private String course;
    private String name;
    private String image;
    private Integer rank;
    private Integer studyTime;
    private Integer totalTime;

    private RankDataResponse(
            Long id,
            String course,
            String name,
            String image,
            Integer rank,
            Integer studyTime,
            Integer totalTime) {
        this.id = id;
        this.course = course;
        this.name = name;
        this.image = image;
        this.rank = rank;
        this.studyTime = studyTime;
        this.totalTime = totalTime;
    }

    public static RankDataResponse of(Member member, Integer rank, Integer time, Integer totalTime) {
        return new RankDataResponse(
                member.getId(),
                member.getCourse().getName(),
                member.getName(),
                member.getImage(),
                rank,
                time,
                totalTime);
    }
}
