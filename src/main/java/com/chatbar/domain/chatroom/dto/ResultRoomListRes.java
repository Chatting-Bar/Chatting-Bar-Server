package com.chatbar.domain.chatroom.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResultRoomListRes {

    private String searchWord;

    private Object data;

    @Builder
    public ResultRoomListRes(String searchWord, Object data) {
        this.searchWord = searchWord;
        this.data = data;
    }

}


