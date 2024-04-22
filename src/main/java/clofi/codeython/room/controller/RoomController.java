package clofi.codeython.room.controller;

import clofi.codeython.room.controller.response.CreateRoomResponse;
import clofi.codeython.room.domain.request.CreateRoomRequest;
import clofi.codeython.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/api/rooms")
    public ResponseEntity<CreateRoomResponse> createRoom(
            @RequestBody CreateRoomRequest createRoomRequest ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.createRoom(createRoomRequest));
    }

}
