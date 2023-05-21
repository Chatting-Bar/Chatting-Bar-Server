package com.chatbar.domain.chatroom.dto;

import com.chatbar.domain.common.Category;
import com.chatbar.domain.user.domain.User;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Data
public class RoomListRes {

    private Long id;

    private String name;

    private String hostName;

    private String participant;

    private String time;

    @Lob
    private EnumSet<Category> categories;


    @Builder
    public RoomListRes(Long id, String name, String hostName, int current, int max, LocalDateTime open, LocalDateTime close, EnumSet<Category> categories) {
        this.id = id;
        this.name = name;
        this.hostName = hostName;
        this.participant = current + " / " + max;
        this.time = open + " ~ " + close;
        this.categories = categories;
    }
}
