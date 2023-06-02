package com.chatbar.domain.chatroom.dto;

import com.chatbar.domain.chatroom.domain.UserChatRoom;
import lombok.Builder;
import lombok.Data;

@Data
public class UserListRes {

    private Long id;

    private String nickname;

    private String profileImg;

    private UserChatRoom.Role userRole;

    private boolean isFrozen; //얼려진 상태인지 -> false면 말할 수 있음.

    @Builder
    public UserListRes(Long id, String nickname, String profileImg, UserChatRoom.Role userRole, boolean isFrozen) {
        this.id = id;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.userRole = userRole;
        this.isFrozen = isFrozen;
    }
}
