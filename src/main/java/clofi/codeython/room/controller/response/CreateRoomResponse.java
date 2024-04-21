package clofi.codeython.room.controller.response;

import clofi.codeython.room.domain.Room;

public record CreateRoomResponse(
        Long roomId,
        String password
) {
    public static CreateRoomResponse of(Room room) {
        return new CreateRoomResponse(
                room.getRoomNo(),
                room.getPassword()
        );
    }
}
