package com.woohaengshi.backend.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class RanksResponse {
    private Boolean hasNext;
    private List<RankDataResponse> ranks;

    private RanksResponse(Boolean hasNext, List<RankDataResponse> ranks) {
        this.hasNext = hasNext;
        this.ranks = ranks;
    }

    public static RanksResponse of(boolean hasNext, List<RankDataResponse> ranks) {
        return new RanksResponse(hasNext, ranks);
    }
}
