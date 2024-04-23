package clofi.codeython.socket.controller.response;

import clofi.codeython.member.domain.Member;

public record SocketUserResponse(
    String nickname,
    Integer level,
    Integer exp
) {
    public static SocketUserResponse of(Member member, Integer level, Integer exp) {
        return new SocketUserResponse(
            member.getNickname(),
            level,
            exp
        );
    }
}
