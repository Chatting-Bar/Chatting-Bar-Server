package com.chatbar.domain.user.domain.repository;

import com.chatbar.domain.user.domain.Subscribe;
import com.chatbar.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe,Long> {

    Optional<Subscribe> findSubscribeByFromUserAndToUser(User fromUser, User toUser);

}
