package clofi.codeython.room.domain;

import clofi.codeython.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="room_member_no", nullable = false)
    private Long roomMemberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_no", nullable = false)
    private Room room;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private Member user;

    @Column(name = "is_owner")
    private boolean isOwner;
}
