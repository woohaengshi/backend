package com.woohaengshi.backend.dto.response;

import com.woohaengshi.backend.domain.member.Member;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingSnapshotResponse {

    private MemberRankingResponse member;
    private RankingResultsResponse infiniteScrolling;

    private RankingSnapshotResponse(
            MemberRankingResponse member, RankingResultsResponse rankingResultsResponse) {
        this.member = member;
        this.infiniteScrolling = rankingResultsResponse;
    }

    public static RankingSnapshotResponse of(
            Member member,
            Integer rank,
            Integer time,
            Integer totalTime,
            Boolean hasNext,
            List<RankingDataResponse> ranks) {
        MemberRankingResponse memberDto = MemberRankingResponse.of(member, rank, time, totalTime);
        RankingResultsResponse infiniteScrolling = RankingResultsResponse.of(hasNext, ranks);
        return new RankingSnapshotResponse(memberDto, infiniteScrolling);
    }
}
