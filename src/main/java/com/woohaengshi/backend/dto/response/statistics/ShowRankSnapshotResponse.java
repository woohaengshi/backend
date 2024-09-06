package com.woohaengshi.backend.dto.response.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.woohaengshi.backend.domain.member.Member;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowRankSnapshotResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RankDataResponse member;

    private RanksResponse ranking;

    private ShowRankSnapshotResponse(RankDataResponse member, RanksResponse ranksResponse) {
        this.member = member;
        this.ranking = ranksResponse;
    }

    public static ShowRankSnapshotResponse of(
            Member member,
            Integer rank,
            Integer time,
            Integer totalTime,
            Boolean hasNext,
            List<RankDataResponse> ranks) {
        RankDataResponse memberDto = RankDataResponse.of(member, rank, time, totalTime);
        RanksResponse infiniteScrolling = RanksResponse.of(hasNext, ranks);
        return new ShowRankSnapshotResponse(memberDto, infiniteScrolling);
    }

    public static ShowRankSnapshotResponse of(Boolean hasNext, List<RankDataResponse> ranks) {
        return new ShowRankSnapshotResponse(null, RanksResponse.of(hasNext, ranks));
    }
}
