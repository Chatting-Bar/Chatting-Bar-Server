package com.chatbar.domain.user.domain.repository;

import com.chatbar.domain.user.domain.Follow;
import com.chatbar.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    Optional<Follow> findFollowByFromUserAndToUser(User fromUser, User toUser);

    List<Follow> findAllByFromUser(User fromUser);

    List<Follow> findAllByToUser(User toUser);

}
