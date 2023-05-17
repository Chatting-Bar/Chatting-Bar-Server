package com.chatbar.domain.chatroom.domain.repository;

import com.chatbar.domain.chatroom.domain.ChatRoom;
import com.chatbar.domain.chatroom.domain.UserChatRoom;
import com.chatbar.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    Optional<UserChatRoom> findUserChatRoomByUserAndChatRoom(User user, ChatRoom chatRoom);
}
