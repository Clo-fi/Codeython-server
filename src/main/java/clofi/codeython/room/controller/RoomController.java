package clofi.codeython.room.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import clofi.codeython.member.service.dto.CustomMemberDetails;
import clofi.codeython.room.controller.response.RoomResponse;
import clofi.codeython.room.service.RoomService;
import clofi.codeython.room.service.request.WaitRoomRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/api/rooms/{roomId}")
    public RoomResponse joinRoomWithPassword(
        @RequestBody WaitRoomRequest request,
        @PathVariable Long roomId,
        @AuthenticationPrincipal CustomMemberDetails userDetails) {
        return roomService.joinRoomWithPassword(request, roomId, userDetails);
    }

    @PostMapping("/api/rooms/direct/{inviteCode}")
    public RoomResponse joinRoomWithInviteCode(
        @PathVariable String inviteCode,
        @AuthenticationPrincipal CustomMemberDetails userDetails) {
        return roomService.joinRoomWithInviteCode(inviteCode, userDetails);
    }
}
