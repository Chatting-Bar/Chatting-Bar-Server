package com.chatbar.domain.chatroom.dto;

import com.chatbar.domain.common.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Data
public class RoomListRes {

    private Long id;

    private String name;

    private String desc;

    private Long hostId;

    private String hostName;

    private String participant;

    private boolean isFull;

    private String time;

    private String[] categories;

    private boolean isPrivate;

    private String password;

//    @Enumerated(value = EnumType.STRING)
    private Status status;


    @Builder
    public RoomListRes(Long id, String name, String desc, Long hostId, String hostName, int current, int max, boolean isFull, LocalDateTime open, LocalDateTime close, String[] categories, boolean isPrivate, String password, Status status) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.hostId = hostId;
        this.hostName = hostName;
        this.participant = current + " / " + max;
        this.isFull = isFull;
        this.time = open + " ~ " + close;
        this.categories = categories;
        this.isPrivate = isPrivate;
        this.password = password;
        this.status = status;
    }
}
