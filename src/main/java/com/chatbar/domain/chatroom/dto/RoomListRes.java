package com.chatbar.domain.chatroom.dto;

import com.chatbar.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomListRes {

    private Long id;

    private String name;

    private String hostName;

    private String participant;

    private String time;


    @Builder
    public RoomListRes(Long id, String name, String hostName, int current, int max, LocalDateTime open, LocalDateTime close) {
        this.id = id;
        this.name = name;
        this.hostName = hostName;
        this.participant = current + " / " + max;
        this.time = open + " ~ " + close;
    }
}
