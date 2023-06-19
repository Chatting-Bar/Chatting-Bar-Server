package com.chatbar.domain.user.domain;

import com.chatbar.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Where(clause = "status = 'ACTIVE'")
public class Follow extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromUser_id")
    private User fromUser;  //구독하는 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toUser_id")
    private User toUser;    //구독받는 유저

    @Builder
    public Follow(Long id, User fromUser, User toUser) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
