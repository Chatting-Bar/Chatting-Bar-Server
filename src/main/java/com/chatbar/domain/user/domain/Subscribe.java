package com.chatbar.domain.user.domain;

import com.chatbar.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Where(clause = "status = 'ACTIVE'")
public class Subscribe extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "fromUserId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;  //구독하는 유저

    @JoinColumn(name = "toUserId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User toUser;    //구독받는 유저

    @Builder
    public Subscribe(Long id, User fromUser, User toUser) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
