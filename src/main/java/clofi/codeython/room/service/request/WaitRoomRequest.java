package clofi.codeython.room.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitRoomRequest {
    private String password;

    public WaitRoomRequest(String password) {
        this.password = password;
    }
}
