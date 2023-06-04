package com.chatbar.domain.user.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class EmailRes {

    private String email;

    @Builder
    public EmailRes(String email) {
        this.email = email;
    }
}
