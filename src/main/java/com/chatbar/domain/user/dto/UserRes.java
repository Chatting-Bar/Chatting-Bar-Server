package com.chatbar.domain.user.dto;

import com.chatbar.domain.common.Category;
import com.chatbar.domain.common.CategorySetConverter;
import jakarta.persistence.Convert;
import lombok.Builder;
import lombok.Data;

import java.util.EnumSet;

@Data
public class UserRes {

    private Long id;

    private String nickname;

    private String email;

    private String profileImg;

    private String[] categories;

    @Builder
    public UserRes(Long id, String nickname, String email, String profileImg, String[] categories) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
        this.categories = categories;
    }
}