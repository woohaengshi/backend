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
public class StatisticsReadDto {

    private MemberDto member;
    private InfiniteScrollingDto infiniteScrolling;

    public static StatisticsReadDto of(
            Member member,
            int rank,
            int dailyTime,
            int totalTime,
            boolean hasNext,
            List<MemberRankDto> ranks) {
        MemberDto memberDto =
                MemberDto.of(
                        member.getCourse().getName(),
                        member.getName(),
                        member.getImage(),
                        rank,
                        dailyTime,
                        totalTime);
        InfiniteScrollingDto infiniteScrolling = InfiniteScrollingDto.of(hasNext, ranks);
        return StatisticsReadDto.builder()
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
        private int rank;
        private int dailyTime;
        private int totalTime;

        public static MemberDto of(
                String course, String name, String image, int rank, int dailyTime, int totalTime) {
            return MemberDto.builder()
                    .course(course)
                    .name(name)
                    .image(image)
                    .rank(rank)
                    .dailyTime(dailyTime)
                    .totalTime(totalTime)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfiniteScrollingDto {
        private boolean hasNext;
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
        private int rank;
        private int time;
        private int totalTime;

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
