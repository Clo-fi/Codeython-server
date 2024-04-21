package clofi.codeython.room.repository;

import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Integer> {
    List<RoomMember> findAllByRoom(Room room);
}
