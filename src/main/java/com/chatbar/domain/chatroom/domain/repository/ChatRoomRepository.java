package com.chatbar.domain.chatroom.domain.repository;

import com.chatbar.domain.chatroom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
