package com.chatbar.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class VerifyRes {

    private String email;

    private String code;

    @Builder
    public VerifyRes(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
