package com.chatbar.domain.chatroom.domain;

import com.chatbar.domain.common.BaseEntity;
import com.chatbar.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserChatRoom extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    @Builder
    public UserChatRoom(Long id, User user, ChatRoom chatRoom) {
        this.id = id;
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
