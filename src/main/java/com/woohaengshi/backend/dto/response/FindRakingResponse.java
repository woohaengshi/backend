package com.woohaengshi.backend.dto.response;

import com.woohaengshi.backend.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindRakingResponse {

    private MemberDto member;
    private InfiniteScrollingDto infiniteScrolling;

    public static FindRakingResponse of(
            Member member,
            Integer rank,
            Integer time,
            Integer totalTime,
            Boolean hasNext,
            List<MemberRankDto> ranks) {
        MemberDto memberDto =
                MemberDto.of(
                        member,
                        rank,
                        time,
                        totalTime);
        InfiniteScrollingDto infiniteScrolling = InfiniteScrollingDto.of(hasNext, ranks);
        return FindRakingResponse.builder()
                .member(memberDto)
                .infiniteScrolling(infiniteScrolling)
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberDto {
        private String course;
        private String name;
        private String image;
        private Integer rank;
        private Integer time;
        private Integer totalTime;

        public static MemberDto of(Member member, int rank, int time, int totalTime) {
            return MemberDto.builder()
                    .course(member.getCourse().getName())
                    .name(member.getName())
                    .image(member.getImage())
                    .rank(rank)
                    .time(time)
                    .totalTime(totalTime)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfiniteScrollingDto {
        private Boolean hasNext;
        private List<MemberRankDto> ranks;

        public static InfiniteScrollingDto of(boolean hasNext, List<MemberRankDto> ranks) {
            return InfiniteScrollingDto.builder().hasNext(hasNext).ranks(ranks).build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRankDto {
        private Long id;
        private String name;
        private String image;
        private String group;
        private Integer rank;
        private Integer time;
        private Integer totalTime;

        public static MemberRankDto of(
                Long id,
                String name,
                String image,
                String group,
                int rank,
                int time,
                int totalTime) {
            return MemberRankDto.builder()
                    .id(id)
                    .name(name)
                    .image(image)
                    .group(group)
                    .rank(rank)
                    .time(time)
                    .totalTime(totalTime)
                    .build();
        }
    }
}
