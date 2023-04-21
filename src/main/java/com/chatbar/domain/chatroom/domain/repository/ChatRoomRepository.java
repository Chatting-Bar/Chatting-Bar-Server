package com.chatbar.domain.chatroom.domain.repository;

import com.chatbar.domain.chatroom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
