package com.woohaengshi.backend.dto.response;

import com.woohaengshi.backend.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberRankingResponse {
    private Long id;
    private String name;
    private String image;
    private String group;
    private Integer rank;
    private Integer time;
    private Integer totalTime;

    private MemberRankingResponse(Long id, String name, String image, String group, Integer rank, Integer time, Integer totalTime) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.group = group;
        this.rank = rank;
        this.time = time;
        this.totalTime = totalTime;
    }

    public static MemberRankingResponse of(
            Member member,
            int rank,
            int time,
            int totalTime) {
        return new MemberRankingResponse(member.getId(), member.getName(), member.getImage(),member.getCourse().getName(), rank, time, totalTime);
    }
}
