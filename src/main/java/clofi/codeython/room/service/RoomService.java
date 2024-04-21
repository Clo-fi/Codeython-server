package clofi.codeython.room.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clofi.codeython.member.domain.Member;
import clofi.codeython.member.repository.MemberRepository;
import clofi.codeython.member.service.dto.CustomMemberDetails;
import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.repository.ProblemRepository;
import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.RoomMember;
import clofi.codeython.room.repository.RoomMemberRepository;
import clofi.codeython.room.repository.RoomRepository;
import clofi.codeython.room.controller.response.RoomResponse;
import clofi.codeython.room.service.request.WaitRoomRequest;
import clofi.codeython.socket.controller.response.SocketUserResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final ProblemRepository problemRepository;
    private final RoomMemberRepository roomMemberRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public RoomResponse joinRoomWithPassword(WaitRoomRequest request, Long roomId, CustomMemberDetails userDetails) {
        Member member = memberRepository.findByUsername(userDetails.getUsername());
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("방이 존재하지 않습니다."));

        if (request.getPassword() != null && !room.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }

        return processRoomJoin(room, member);
    }

    public RoomResponse joinRoomWithInviteCode(String inviteCode, CustomMemberDetails userDetails) {
        Member member = memberRepository.findByUsername(userDetails.getUsername());
        Room room = roomRepository.findByInviteCode(inviteCode);
        if (room == null) {
            throw new IllegalArgumentException("방이 존재하지 않습니다");
        }
        return processRoomJoin(room, member);
    }

    private RoomResponse processRoomJoin(Room room, Member member) {
        Problem problem = problemRepository.findByProblemNo(room.getProblem().getProblemNo());
        RoomMember roomMember = new RoomMember(room, member, false);
        roomMemberRepository.save(roomMember);
        notifyRoomParticipants(room, member);

        return RoomResponse.of(room, problem);
    }

    private void notifyRoomParticipants(Room room, Member member) {
        SocketUserResponse socketUserResponse = new SocketUserResponse(member.getNickname(), member.getExp());
        messagingTemplate.convertAndSend("/topic/rooms/" + room.getRoomNo(), socketUserResponse);
    }
}
