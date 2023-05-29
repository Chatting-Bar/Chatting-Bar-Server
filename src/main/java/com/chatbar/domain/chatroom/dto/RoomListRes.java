package com.chatbar.domain.chatroom.dto;

import com.chatbar.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomListRes {

    private Long id;

    private String name;

    private String participant;

    private String time;

    private User host;

    @Builder
    public RoomListRes(Long id, String name, int current, int max, LocalDateTime open, LocalDateTime close, User host) {
        this.id = id;
        this.name = name;
        this.participant = current + " / " + max;
        this.time = open + " ~ " + close;
        this.host = host;
    }
}
