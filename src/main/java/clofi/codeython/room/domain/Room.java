package clofi.codeython.room.domain;

import clofi.codeython.problem.domain.Problem;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_no", nullable = false)
    private Long roomNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_no", nullable = false)
    private Problem problem;

    @Column(name = "room_name", length = 50)
    private String roomName;

    @Column(name = "password", length = 4)
    @Size(min = 4, max = 4, message = "비밀번호는 4자리입니다.")
    private String password;

    @Column(name = "is_secret")
    private boolean isSecret;

    @Column(name = "is_solo_play")
    private boolean isSoloPlay;

    @Column(name = "invite_code", nullable = false, length = 50)
    private String inviteCode;

    @Column(name = "limit_member_cnt", nullable = false)
    private int limitMemberCnt;

    public Room(String roomName, Problem problem, int limitMemberCnt, boolean isSecret, String password,
                boolean isSoloPlay, String inviteCode) {
        this.roomName = roomName;
        this.problem = problem;
        this.limitMemberCnt = limitMemberCnt;
        this.isSecret = isSecret;
        this.password = password;
        this.isSoloPlay = isSoloPlay;
        this.inviteCode = inviteCode;
    }
}
