package clofi.codeython.socket.controller.response;

import clofi.codeython.member.domain.Member;

public record SocketUserResponse(
    String nickname,
    Integer exp
) {
    public static SocketUserResponse of(Member member) {
        return new SocketUserResponse(
            member.getNickname(),
            member.getExp()
        );
    }
}
