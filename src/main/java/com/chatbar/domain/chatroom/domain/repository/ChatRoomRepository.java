package com.chatbar.domain.chatroom.domain.repository;

import com.chatbar.domain.chatroom.domain.ChatRoom;
import com.chatbar.domain.common.Status;
import com.chatbar.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAllByHostName(String hostName);

    List<ChatRoom> findAllByName(String name);

    List<ChatRoom> findAllByStatus(Status status);
}
