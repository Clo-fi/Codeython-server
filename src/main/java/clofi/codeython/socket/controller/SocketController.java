package clofi.codeython.socket.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import clofi.codeython.socket.controller.response.SocketUserResponse;
import clofi.codeython.socket.service.SocketService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SocketController {
    private final SocketService socketService;

    @MessageMapping("/room/{roomId}/join")
    @SendTo("/topic/rooms/{roomId}")
    public List<SocketUserResponse> joinRoomWithPassword(
        @DestinationVariable Long roomId) {
        return socketService.joinRoom(roomId);
    }
}

