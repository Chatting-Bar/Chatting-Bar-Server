package com.chatbar.domain.chatroom.domain.repository;

import com.chatbar.domain.chatroom.domain.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
}
