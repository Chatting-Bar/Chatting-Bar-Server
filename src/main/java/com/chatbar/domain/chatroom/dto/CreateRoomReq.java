package com.chatbar.domain.chatroom.dto;

import com.chatbar.domain.common.Category;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Data
public class CreateRoomReq {

    @NotBlank
    private String name;

    private String desc;

    private EnumSet<Category> categories;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    private int maxParticipant;

    private Boolean isPrivate = Boolean.FALSE;

    @Nullable
    private String password;

}
