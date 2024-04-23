package clofi.codeython.socket.controller;

import clofi.codeython.socket.controller.response.DataResponse;
import clofi.codeython.socket.controller.response.DataType;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public DataResponse<List<SocketUserResponse>> joinRoom(
            @DestinationVariable Long roomId) {
        return new DataResponse<>(socketService.joinRoom(roomId), DataType.USER);
    }

    @MessageMapping("/room/{roomId}/leave")
    @SendTo("/sub/room/{roomId}")
    public DataResponse<List<SocketUserResponse>> leaveRoom(
            @DestinationVariable Long roomId,
            @Header("nickname") String nickName
    ) {
        return new DataResponse<>(socketService.leaveRoom(roomId, nickName), DataType.USER);
    }

    @MessageMapping("/room/{roomId}/chat")
    @SendTo("/sub/room/{roomId}")
    public DataResponse<ChatMessage> sendChatMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessage chatMessage
    ) {
        return new DataResponse<>(chatMessage, DataType.CHAT);
    }

    @MessageMapping("/room/{roomId}/gameStart")
    @SendTo("/sub/room/{roomId}")
    public DataResponse<String> gameStartMessage(
            @DestinationVariable Long roomId
    ) {
        return new DataResponse<>("게임이 시작되었습니다", DataType.GAME_START);
    }
}
