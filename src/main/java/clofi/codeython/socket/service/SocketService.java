package clofi.codeython.socket.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.RoomMember;
import clofi.codeython.room.repository.RoomMemberRepository;
import clofi.codeython.room.repository.RoomRepository;
import clofi.codeython.socket.controller.response.SocketUserResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SocketService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;

    public List<SocketUserResponse> joinRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("방이 존재하지 않습니다."));
        List<RoomMember> roomMemberList = roomMemberRepository.findAllByRoomRoomNo(room.getRoomNo());

        return roomMemberList.stream().map(
            m -> SocketUserResponse.of(m.getUser())
        ).toList();
    }
}
