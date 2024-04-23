package clofi.codeython.member.controller.response;

import java.util.List;

public record RankingResponse(
        List<RankerResponse> ranker,
        int myRank
) {
    public static RankingResponse of(
            List<RankerResponse> ranker, int myRank) {
        return new RankingResponse(
                ranker,
                myRank
        );
    }
}
