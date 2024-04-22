package clofi.codeython.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import clofi.codeython.member.domain.Member;
import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.RoomMember;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Integer> {
    List<RoomMember> findAllByRoom(Room room);

    List<RoomMember> findAllByRoomRoomNo(Long roomNo);

    void deleteByRoomAndUser(Room room, Member user);
}
