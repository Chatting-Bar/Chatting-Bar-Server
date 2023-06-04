package com.chatbar.domain.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyRes {

    private String email;
    private String code;

}
