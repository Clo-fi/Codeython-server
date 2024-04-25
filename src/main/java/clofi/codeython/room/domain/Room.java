package clofi.codeython.room.domain;

import clofi.codeython.problem.core.domain.Problem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "room_name", unique = true, length = 50)
    private String roomName;

    @Column(name = "password", length = 4)
    @Size(min = 4, max = 4, message = "비밀번호는 4자리입니다.")
    private String password;

    @Column(name = "is_secret")
    private boolean isSecret;

    @Column(name = "is_solo_play")
    private boolean isSoloPlay;

    @Column(name = "invite_code", unique = true, nullable = false, length = 50)
    private String inviteCode;

    @Column(name = "limit_member_cnt", nullable = false)
    private int limitMemberCnt;

    @Column(name = "player_count")
    private Integer playerCount;

    @Column(name = "is_play", nullable = false)
    private boolean isPlay;

    public Room(String roomName, Problem problem, int limitMemberCnt, boolean isSecret, String password,
        boolean isSoloPlay, String inviteCode) {
        this.roomName = roomName;
        this.problem = problem;
        this.limitMemberCnt = limitMemberCnt;
        this.isSecret = isSecret;
        this.password = password;
        this.isSoloPlay = isSoloPlay;
        this.inviteCode = inviteCode;
        this.isPlay = false;
    }

    public void changeProblem(Problem problem) {
        this.problem = problem;
    }

    public void updatePlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void gameEnd() {
        this.isPlay = false;
    }

    public void gameStart() {
        this.isPlay = true;
    }
}
