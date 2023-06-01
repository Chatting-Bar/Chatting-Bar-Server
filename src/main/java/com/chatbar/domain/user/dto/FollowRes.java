package com.chatbar.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FollowRes {

    private Long id;

    private String nickname;

    private String email;

    @Builder
    public FollowRes(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}
