package clofi.codeython.room.controller.response;

import clofi.codeython.problem.domain.Problem;
import clofi.codeython.room.domain.Room;

public record RoomResponse(
    Long roomId,
    Long problemId,
    String problemTitle,
    Integer limitTime,
    Integer difficulty,
    String roomName,
    Integer limitMemberCnt,
    String inviteCode,
    Boolean isSoloPlay
) {
    public static RoomResponse of(Room room, Problem problem) {
        return new RoomResponse(
            room.getRoomNo(),
            problem.getProblemNo(),
            problem.getTitle(),
            problem.getLimitTime(),
            problem.getDifficulty(),
            room.getRoomName(),
            room.getLimitMemberCnt(),
            room.getInviteCode(),
            room.isSoloPlay()
        );
    }

}

