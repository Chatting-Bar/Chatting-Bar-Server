package com.chatbar.domain.chatroom.domain;

import com.chatbar.domain.common.BaseEntity;
import com.chatbar.domain.common.Category;
import com.chatbar.domain.common.CategorySetConverter;
import com.chatbar.domain.user.domain.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Where(clause = "status = 'ACTIVE'")
@Getter
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User host;

    private String hostName;

    @NotNull
    private String name;

    private String desc;

    @Nullable
    @Convert(converter = CategorySetConverter.class)
    private EnumSet<Category> categories = EnumSet.noneOf(Category.class);

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    private int maxParticipant = 16;

    private int currentParticipant;

    private boolean isFull = false;

    private boolean isPrivate = false;

    private String password;

    @Builder
    public ChatRoom(Long id, User host, String name, String desc, EnumSet<Category> categories, LocalDateTime open, LocalDateTime close, int max, int current, boolean isPrivate, String password){
        this.id = id;
        this.host = host;
        this.hostName = host.getNickname();
        this.name = name;
        this.desc = desc;
        this.categories = categories;
        this.openTime = open;
        this.closeTime = close;
        this.maxParticipant = max;
        this.currentParticipant = current;
        this.isFull = false;
        this.isPrivate = isPrivate;
        this.password = password;
    }

    public void updateName(String name){this.name = name;}

    public void updateDesc(String desc){this.desc = desc;}

    public void updateCategories(EnumSet<Category> newCategories){this.categories = newCategories;}

    public void updateOpenTime(LocalDateTime openTime){this.openTime = openTime;}

    public void updateCloseTime(LocalDateTime closeTime){this.closeTime = closeTime;}

    public void updateMaxParticipant(int maxParticipant){this.maxParticipant = maxParticipant;}

    public void updateCurrentParticipant(int currentParticipant){

        if (currentParticipant < maxParticipant) {  //가득 찬 경우가 아니면
            this.currentParticipant = currentParticipant;
            //isFull이 true였다가 false로 변하는 경우 (ex.퇴장, 강퇴)
            if(this.isFull){
                this.isFull = false;
            }
        }
        else{  //가득 찬 경우
            this.isFull = true;
            if (currentParticipant == maxParticipant) {
                this.currentParticipant = currentParticipant;
            }
        }
    }

    public void updateIsPrivate(boolean isPrivate){this.isPrivate = isPrivate;}

    public void updatePassword(String password){this.password = password;}
}
