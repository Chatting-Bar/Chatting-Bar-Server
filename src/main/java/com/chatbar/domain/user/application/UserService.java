package com.chatbar.domain.user.application;

import com.chatbar.domain.common.Category;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void updateCategories(UserPrincipal userPrincipal, EnumSet<Category> newCategories){
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + userPrincipal.getId() + " not found"));
        user.updateCategories(newCategories);
    }

    public UserPrincipal getUserPrincipalByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found"));
        return UserPrincipal.create(user);
    }

    @Transactional
    public void updatePassword(UserPrincipal userPrincipal, String newPassword) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.updatePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void updatePasswordByEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found"));
        user.updatePassword(passwordEncoder.encode(newPassword));
    }

}
