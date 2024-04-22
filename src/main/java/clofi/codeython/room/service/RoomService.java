package clofi.codeython.room.service;

import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.repository.ProblemRepository;
import clofi.codeython.room.controller.response.CreateRoomResponse;
import clofi.codeython.room.domain.Room;
import clofi.codeython.room.domain.request.CreateRoomRequest;
import clofi.codeython.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final ProblemRepository problemRepository;

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

        if (createRoomRequest.getLimitMemberCnt() == 2 || createRoomRequest.getLimitMemberCnt() == 4
        || createRoomRequest.getLimitMemberCnt() == 6){
            Problem problem = problemRepository.findByProblemNo(createRoomRequest.getProblemId());

            UUID uuid = UUID.randomUUID();
            String inviteCode = uuid.toString().substring(0, uuid.toString().indexOf("-"));

            Room room = roomRepository.save(createRoomRequest.toRoom(problem,inviteCode));

            return CreateRoomResponse.of(room);
        } else {
            throw new IllegalArgumentException("인원 제한 수는 2, 4, 6 중 하나여야 합니다.");
        }
    }
}
