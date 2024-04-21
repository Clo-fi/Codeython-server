package clofi.codeython.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.RoomMember;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    List<RoomMember> findAllByRoomRoomNo(Long roomNo);
}
