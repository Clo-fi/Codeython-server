package clofi.codeython.room.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import clofi.codeython.room.domain.Room;
    
import clofi.codeython.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    boolean existsByRoomName(String roomName);
    Room findByInviteCode(String inviteCode);
}
