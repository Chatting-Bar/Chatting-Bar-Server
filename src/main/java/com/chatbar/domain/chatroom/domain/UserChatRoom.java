package com.chatbar.domain.chatroom.domain;

import com.chatbar.domain.common.BaseEntity;
import com.chatbar.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Where(clause = "status = 'ACTIVE'")
@Entity
public class UserChatRoom extends BaseEntity {

    public enum Role{
        HOST, GUEST
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Role userRole; // 방장 or 손님

    private boolean isFrozen; //얼려진 상태인지 -> false면 말할 수 있음.

    private LocalDateTime startFrozenTime; //얼려진 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    @Builder
    public UserChatRoom(Long id, User user, ChatRoom chatRoom, Role userRole, boolean isFrozen) {
        this.id = id;
        this.user = user;
        this.chatRoom = chatRoom;
        this.userRole = userRole;
        this.isFrozen = isFrozen;
        this.startFrozenTime = LocalDateTime.now();
    }

    public void updateIsFrozen(boolean isFrozen) {
        this.startFrozenTime = LocalDateTime.now();
        this.isFrozen = isFrozen;
    }
}
