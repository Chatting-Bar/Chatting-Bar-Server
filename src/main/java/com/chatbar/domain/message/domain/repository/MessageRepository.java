package com.chatbar.domain.message.domain.repository;

import com.chatbar.domain.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
