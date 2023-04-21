package com.chatbar.domain.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  // 클래스에 Auditing(자동 시간값 주입) 기능 추가
public abstract class BaseEntity {

    // Entity가 생성되어 저장될 때 시간 자동 저장
    @CreatedDate
    private LocalDateTime createdAt;

    // 조회한 Entity 값을 변경할 때 시간 자동 저장
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Enumerated(value = EnumType.STRING)
    private Status status = Status.valueOf("ACTIVE");

    public void updateStatus(Status status){
        this.status = status;

    }
}
