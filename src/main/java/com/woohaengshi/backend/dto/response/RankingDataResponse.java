package com.woohaengshi.backend.dto.response;

import com.woohaengshi.backend.domain.member.Member;
import lombok.Getter;

@Getter
public class RankingDataResponse {
    private String course;
    private String name;
    private String image;
    private Integer rank;
    private Integer time;
    private Integer totalTime;

    private RankingDataResponse(String course, String name, String image, Integer rank, Integer time, Integer totalTime) {
        this.course = course;
        this.name = name;
        this.image = image;
        this.rank = rank;
        this.time = time;
        this.totalTime = totalTime;
    }

    public static RankingDataResponse of(Member member, int rank, int time, int totalTime) {
        return new RankingDataResponse(
                member.getCourse().getName(),
                member.getName(),
                member.getImage(),
                rank,
                time,
                totalTime);
    }
}
