package clofi.codeython.socket.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import clofi.codeython.socket.controller.response.ChatMessage;
import clofi.codeython.socket.controller.response.SocketUserResponse;
import clofi.codeython.socket.service.SocketService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SocketController {
    private final SocketService socketService;

    @MessageMapping("/room/{roomId}/join")
    @SendTo("/sub/room/{roomId}")
    public List<SocketUserResponse> joinRoom(
        @DestinationVariable Long roomId,
        @Header("nickname") String nickName,
        StompHeaderAccessor accessor) {

        return socketService.joinRoom(roomId);
    }

    @MessageMapping("/room/{roomId}/leave")
    @SendTo("/sub/room/{roomId}")
    public List<SocketUserResponse> leaveRoom(
        @DestinationVariable Long roomId,
        @Header("nickname") String nickName,
        StompHeaderAccessor accessor
    ) {
        return socketService.leaveRoom(roomId, nickName);
    }

    @MessageMapping("/room/{roomId}/chat")
    @SendTo("/sub/room/{roomId}/chat")
    public ChatMessage sendChatMessage(
        @DestinationVariable Long roomId,
        @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

}

