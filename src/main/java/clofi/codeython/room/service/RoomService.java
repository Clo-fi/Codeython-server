package clofi.codeython.room.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clofi.codeython.member.domain.Member;
import clofi.codeython.member.repository.MemberRepository;
import clofi.codeython.member.service.dto.CustomMemberDetails;
import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.repository.ProblemRepository;
import clofi.codeython.room.controller.response.AllRoomResponse;
import clofi.codeython.room.controller.response.CreateRoomResponse;
import clofi.codeython.room.controller.response.RoomResponse;
import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.RoomMember;
import clofi.codeython.room.domain.request.CreateRoomRequest;
import clofi.codeython.room.repository.RoomMemberRepository;
import clofi.codeython.room.repository.RoomRepository;
import clofi.codeython.room.service.request.WaitRoomRequest;
import clofi.codeython.socket.controller.response.SocketUserResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final ProblemRepository problemRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRepository memberRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public RoomResponse joinRoomWithPassword(WaitRoomRequest request, Long roomId, CustomMemberDetails userDetails) {
        Member member = memberRepository.findByUsername(userDetails.getUsername());
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("방이 존재하지 않습니다."));

        List<RoomMember> roomMember = roomMemberRepository.findAllByRoomRoomNo(room.getRoomNo());

        if (request.getPassword() != null && !room.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀립니다");
        }
        if (roomMember.size() >= room.getLimitMemberCnt()) {
            throw new IllegalArgumentException("방이 꽉 찼습니다");
        }
        if (roomMember.size() == 0) {
            return processRoomJoin(room, member, true);
        }
        return processRoomJoin(room, member, false);
    }

    public RoomResponse joinRoomWithInviteCode(String inviteCode, CustomMemberDetails userDetails) {
        Member member = memberRepository.findByUsername(userDetails.getUsername());
        Room room = roomRepository.findByInviteCode(inviteCode);
        if (room == null) {
            throw new IllegalArgumentException("방이 존재하지 않습니다");
        }
        List<RoomMember> roomMember = roomMemberRepository.findAllByRoomRoomNo(room.getRoomNo());
        if (roomMember.size() >= room.getLimitMemberCnt()) {
            throw new IllegalArgumentException("방이 꽉 찼습니다");
        }
        return processRoomJoin(room, member, false);
    }

    public CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest) {
        if (roomRepository.existsByRoomName(createRoomRequest.getRoomName())) {
            throw new IllegalArgumentException("이미 만들어진 경기장 이름입니다.");
        }

        try {
            Integer.valueOf(createRoomRequest.getPassword());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("비밀번호는 숫자여야 합니다.");
        }

        if (createRoomRequest.getPassword().length() != 4) {
            throw new IllegalArgumentException("비밀번호는 4자리여야 합니다.");
        }

        if (!(createRoomRequest.getLimitMemberCnt() == 2 || createRoomRequest.getLimitMemberCnt() == 4
            || createRoomRequest.getLimitMemberCnt() == 6)) {
            throw new IllegalArgumentException("인원 제한 수는 2, 4, 6 중 하나여야 합니다.");
        }
        Problem problem = problemRepository.findByProblemNo(createRoomRequest.getProblemId());

        UUID uuid = UUID.randomUUID();
        String inviteCode = uuid.toString().substring(0, uuid.toString().indexOf("-"));

        Room room = roomRepository.save(createRoomRequest.toRoom(problem, inviteCode));

        return CreateRoomResponse.of(room);
    }

    public List<AllRoomResponse> getAllRoom() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
            .map(room -> {
                List<RoomMember> roomMembers = roomMemberRepository.findAllByRoom(room);
                int playMemberCount = roomMembers.size();
                return AllRoomResponse.of(room, playMemberCount);
            })
            .collect(Collectors.toList());
    }

    private RoomResponse processRoomJoin(Room room, Member member, Boolean isOwner) {
        Problem problem = problemRepository.findByProblemNo(room.getProblem().getProblemNo());
        RoomMember roomMember = new RoomMember(room, member, isOwner);
        roomMemberRepository.save(roomMember);
        notifyRoomParticipants(room, member);

        return RoomResponse.of(room, problem);
    }

    private void notifyRoomParticipants(Room room, Member member) {
        SocketUserResponse socketUserResponse = new SocketUserResponse(member.getNickname(), member.getExp());
        messagingTemplate.convertAndSend("/topic/rooms/" + room.getRoomNo(), socketUserResponse);
    }
}
