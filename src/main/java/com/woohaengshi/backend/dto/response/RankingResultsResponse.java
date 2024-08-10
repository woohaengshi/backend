package com.woohaengshi.backend.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class RankingResultsResponse {
    private Boolean hasNext;
    private List<RankingDataResponse> ranks;

    private RankingResultsResponse(Boolean hasNext, List<RankingDataResponse> ranks) {
        this.hasNext = hasNext;
        this.ranks = ranks;
    }

    public static RankingResultsResponse of(boolean hasNext, List<RankingDataResponse> ranks) {
        return new RankingResultsResponse(hasNext, ranks);
    }
}
