package com.chatbar.domain.chatroom.dto;

import com.chatbar.global.payload.Message;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateRoomRes {

    private Long id;

    private Message message;

    @Builder
    public CreateRoomRes(Long id, Message message) {
        this.id = id;
        this.message = message;
    }
}
