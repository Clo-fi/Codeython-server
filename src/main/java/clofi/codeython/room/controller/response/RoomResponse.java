package clofi.codeython.room.controller.response;

import clofi.codeython.problem.domain.Problem;
import clofi.codeython.room.domain.Room;

public record RoomResponse(
        Long roomId,
        String problemTitle,
        Integer limitTime,
        Integer difficulty,
        String roomName,
        String inviteCode,
        Boolean isSoloPlay
) {
    public static RoomResponse of(Room room, Problem problem) {
        return new RoomResponse(
                room.getRoomNo(),
                problem.getTitle(),
                problem.getLimitTime(),
                problem.getDifficulty(),
                room.getRoomName(),
                room.getInviteCode(),
                room.isSoloPlay()
        );
    }

}

