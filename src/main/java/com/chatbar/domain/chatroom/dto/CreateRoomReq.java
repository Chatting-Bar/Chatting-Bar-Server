package com.chatbar.domain.chatroom.dto;

import com.chatbar.domain.common.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.EnumSet;

@Data
public class CreateRoomReq {

    @NotBlank
    private String name;

    private String desc;

    private EnumSet<Category> categories;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime openTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closeTime;

    private int maxParticipant;

    private Boolean isPrivate = Boolean.FALSE;

    @Nullable
    private String password;

}
