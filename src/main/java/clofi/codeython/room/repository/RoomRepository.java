package clofi.codeython.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import clofi.codeython.room.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByInviteCode(String inviteCode);
}
