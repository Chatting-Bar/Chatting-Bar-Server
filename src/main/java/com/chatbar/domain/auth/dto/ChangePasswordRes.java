package com.chatbar.domain.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordRes {

    private String email;

    private String newPassword;

}
