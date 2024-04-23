package clofi.codeython.room.domain;

import clofi.codeython.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_member_no", nullable = false)
    private Long roomMemberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_no", nullable = false)
    private Room room;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private Member user;

    @Column(name = "is_owner")
    private boolean isOwner;

    public RoomMember(Room room, Member user, boolean isOwner) {
        this.room = room;
        this.user = user;
        this.isOwner = isOwner;
    }

    public void updateOwner(boolean b) {
        this.isOwner = b;
    }
}
