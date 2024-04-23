package clofi.codeython.socket.service;

import clofi.codeython.member.domain.Member;
import clofi.codeython.member.repository.MemberRepository;
import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.RoomMember;
import clofi.codeython.room.repository.RoomMemberRepository;
import clofi.codeython.room.repository.RoomRepository;
import clofi.codeython.socket.controller.response.SocketUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class SocketService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;

    public List<SocketUserResponse> joinRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방이 존재하지 않습니다."));
        List<RoomMember> roomMemberList = roomMemberRepository.findAllByRoomRoomNo(room.getRoomNo());

        return roomMemberList.stream().map(m -> {
            Member member = m.getUser();
            Map<String, Integer> levelAndExp = calculateLevelAndExp(member);
            return SocketUserResponse.of(member, levelAndExp.get("level"), levelAndExp.get("exp"));
        }).toList();
    }

    public List<SocketUserResponse> leaveRoom(Long roomId, String nickName) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방이 존재하지 않습니다."));
        Member member = memberRepository.findByNickname(nickName);
        RoomMember roomMemberUser = roomMemberRepository.findByUser(member);
        List<RoomMember> roomMemberList = roomMemberRepository.findAllByRoomRoomNo(room.getRoomNo());

        if (roomMemberUser.isOwner()) {
            roomMemberRepository.deleteByRoomAndUser(room, member);
            if (roomMemberList.size() > 1) {
                RoomMember newOwner =
                        roomMemberList.get(0).getUser().equals(member) ? roomMemberList.get(1) : roomMemberList.get(0);
                newOwner.updateOwner(true);
                roomMemberRepository.save(newOwner);
            } else {
                roomRepository.deleteById(roomId);
            }
        } else {
            roomMemberRepository.deleteByRoomAndUser(room, member);
        }

        roomMemberList.removeIf(m -> m.getUser().equals(member));

        return roomMemberList.stream().map(m -> {
            Member oneMember = m.getUser();
            Map<String, Integer> levelAndExp = calculateLevelAndExp(oneMember);
            return SocketUserResponse.of(oneMember, levelAndExp.get("level"), levelAndExp.get("exp"));
        }).toList();
    }

    private Map<String, Integer> calculateLevelAndExp(Member member) {
        int exp = member.getExp();
        int level = 1;
        if (exp >= 100) {
            level = exp / 100 + 1;
            exp = exp % 100;
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("level", level);
        result.put("exp", exp);
        return result;
    }
}
