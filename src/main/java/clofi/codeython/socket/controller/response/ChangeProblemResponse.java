package clofi.codeython.socket.controller.response;

public record ChangeProblemResponse(
    Long problemNo,
    String problemTitle,
    Integer limitTime,
    Integer difficulty
) {
}
