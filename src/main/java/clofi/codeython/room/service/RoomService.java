package clofi.codeython.room.service;

import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.repository.ProblemRepository;
import clofi.codeython.room.controller.response.AllRoomResponse;
import clofi.codeython.room.controller.response.CreateRoomResponse;
import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.RoomMember;
import clofi.codeython.room.domain.request.CreateRoomRequest;
import clofi.codeython.room.repository.RoomMemberRepository;
import clofi.codeython.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final ProblemRepository problemRepository;
    private final RoomMemberRepository roomMemberRepository;

    public CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest) {
        if (roomRepository.existsByRoomName(createRoomRequest.getRoomName())){
            throw new IllegalArgumentException("이미 만들어진 경기장 이름입니다.");
        }

        try{
            Integer.valueOf(createRoomRequest.getPassword());
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("비밀번호는 숫자여야 합니다.");
        }

        if (createRoomRequest.getPassword().length() != 4){
            throw new IllegalArgumentException("비밀번호는 4자리여야 합니다.");
        }

        if (!(createRoomRequest.getLimitMemberCnt() == 2 || createRoomRequest.getLimitMemberCnt() == 4
                || createRoomRequest.getLimitMemberCnt() == 6)){
            throw new IllegalArgumentException("인원 제한 수는 2, 4, 6 중 하나여야 합니다.");
        }
            Problem problem = problemRepository.findByProblemNo(createRoomRequest.getProblemId());

            UUID uuid = UUID.randomUUID();
            String inviteCode = uuid.toString().substring(0, uuid.toString().indexOf("-"));

            Room room = roomRepository.save(createRoomRequest.toRoom(problem,inviteCode));

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
}
