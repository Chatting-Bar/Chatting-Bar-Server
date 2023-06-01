package com.chatbar.domain.email;

import java.time.LocalDateTime;

public class VerificationCode {
    private String code;
    private LocalDateTime expiryTime;

    public VerificationCode(String code, int expiryMinutes) {
        this.code = code;
        this.expiryTime = LocalDateTime.now().plusMinutes(expiryMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    public String getCode() {
        return code;
    }
}

