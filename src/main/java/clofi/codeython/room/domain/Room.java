package clofi.codeython.room.domain;

import clofi.codeython.problem.domain.Problem;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_no", nullable = false)
    private Problem problem;

    @Column(name = "password", length = 4)
    private String password;

    @Column(name = "is_secret")
    private boolean isSecret;

    @Column(name = "is_soloplay")
    private boolean isSoloplay;

    @Column(name = "invite_code", nullable = false, length = 50)
    private String inviteCode;

    @Column(name = "limit_member_cnt", nullable = false)
    private int limitMemberCnt;

}
